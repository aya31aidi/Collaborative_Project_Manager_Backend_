package com.example.projectmanager;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Goes through the API the way the Angular client does, one HTTP call at a
 * time, so every call runs in its own transaction like in production.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ApiTests {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper json;

    @Test
    void fullFlowFromTeamToBoard() throws Exception {
        long userId = createUser("Lea Weber");
        long teamId = createTeam("Core");
        call(post("/teams/" + teamId + "/addUser/" + userId)).andExpect(status().isOk());
        long projectId = createProject(teamId, "Apollo");
        long sprintId = createSprint(projectId, "2026-10-20", "2026-11-03");

        long taskId = id(call(post("/tasks"), """
                {"title": "Login page", "priority": "HIGH", "sprintId": %d,
                 "assigneeId": %d, "tags": ["frontend", "Frontend ", "auth"]}
                """.formatted(sprintId, userId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("TO_DO"))
                .andExpect(jsonPath("$.sprintId").value(sprintId))
                .andExpect(jsonPath("$.tags", hasSize(2))));

        call(get("/sprints/" + sprintId))
                .andExpect(jsonPath("$.projectName").value("Apollo"))
                .andExpect(jsonPath("$.startDate").value("2026-10-20"))
                .andExpect(jsonPath("$.tasks[0].id").value(taskId))
                .andExpect(jsonPath("$.tasks[0].assignedTo.fullName").value("Lea Weber"))
                .andExpect(jsonPath("$.tasks[0].tags[*].name", containsInAnyOrder("frontend", "auth")));

        call(get("/sprints/" + sprintId + "/users")).andExpect(jsonPath("$", hasSize(1)));

        call(put("/tasks/" + taskId + "/status"), "{\"status\": \"IN_PROGRESS\"}")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void passwordIsNeverSentBack() throws Exception {
        long userId = createUser("Sam Doe");
        call(get("/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void renamingAProjectKeepsItsSprintsAndTeam() throws Exception {
        long teamId = createTeam("Rename team");
        long projectId = createProject(teamId, "Before");
        createSprint(projectId, "2026-10-01", "2026-10-14");

        call(put("/projects/" + projectId), "{\"name\": \"After\", \"description\": \"new\"}")
                .andExpect(status().isOk());

        call(get("/projects/" + projectId))
                .andExpect(jsonPath("$.name").value("After"))
                .andExpect(jsonPath("$.team.id").value(teamId))
                .andExpect(jsonPath("$.sprints", hasSize(1)));
    }

    @Test
    void renamingATeamKeepsItsMembers() throws Exception {
        long userId = createUser("Kim Lee");
        long teamId = createTeam("Old name");
        call(post("/teams/" + teamId + "/addUser/" + userId));
        call(post("/teams/" + teamId + "/addUser/" + userId));

        call(put("/teams/" + teamId), "{\"name\": \"New name\"}").andExpect(status().isOk());

        call(get("/teams/" + teamId + "/users")).andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void aTeamWorksOnOneProjectOnly() throws Exception {
        long teamId = createTeam("Busy team");
        createProject(teamId, "First");
        call(post("/projects/" + teamId), "{\"name\": \"Second\"}").andExpect(status().isConflict());
    }

    @Test
    void deletingATeamKeepsTheProject() throws Exception {
        long teamId = createTeam("Short-lived");
        long projectId = createProject(teamId, "Survivor");

        call(delete("/teams/" + teamId)).andExpect(status().isNoContent());

        call(get("/projects/" + projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.team").value(nullValue()));
    }

    @Test
    void deletingAnAssignedUserUnassignsTheirTasks() throws Exception {
        long userId = createUser("Leaving Soon");
        long teamId = createTeam("Team with leaver");
        call(post("/teams/" + teamId + "/addUser/" + userId));
        long sprintId = createSprint(createProject(teamId, "P"), "2026-10-01", "2026-10-14");
        long taskId = id(call(post("/tasks"), """
                {"title": "Hand over", "sprintId": %d, "assigneeId": %d}
                """.formatted(sprintId, userId)));

        call(delete("/users/" + userId)).andExpect(status().isNoContent());

        call(get("/tasks/" + taskId)).andExpect(jsonPath("$.assignedTo").value(nullValue()));
        call(get("/teams/" + teamId + "/users")).andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void updatingTagsKeepsTheOnesThatStay() throws Exception {
        long sprintId = createSprint(createProject(createTeam("Tag team"), "Tags"), "2026-10-01", "2026-10-14");
        JsonNode task = body(call(post("/tasks"), """
                {"title": "Tagged", "sprintId": %d, "tags": ["bug", "ui"]}
                """.formatted(sprintId)));
        long taskId = task.get("id").asLong();
        long bugTagId = task.get("tags").get(0).get("id").asLong();

        call(put("/tasks/" + taskId), """
                {"title": "Tagged", "sprintId": %d, "tags": ["bug", "backend"]}
                """.formatted(sprintId))
                .andExpect(jsonPath("$.tags[0].id").value(bugTagId))
                .andExpect(jsonPath("$.tags[*].name", containsInAnyOrder("bug", "backend")));

        call(get("/tags/task/" + taskId)).andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void deletingASprintRemovesItsTasks() throws Exception {
        long projectId = createProject(createTeam("Cleanup"), "Cleanup");
        long sprintId = createSprint(projectId, "2026-10-01", "2026-10-14");
        long taskId = id(call(post("/tasks"), "{\"title\": \"Gone\", \"sprintId\": %d, \"tags\": [\"x\"]}".formatted(sprintId)));

        call(delete("/sprints/" + sprintId)).andExpect(status().isNoContent());

        call(get("/tasks/" + taskId)).andExpect(status().isNotFound());
        call(get("/projects/" + projectId)).andExpect(jsonPath("$.sprints", hasSize(0)));
    }

    @Test
    void errorsComeBackAsShortJson() throws Exception {
        call(get("/sprints/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Sprint 999999 was not found"))
                .andExpect(jsonPath("$.trace").doesNotExist());

        call(post("/tasks"), "{\"title\": \"  \", \"sprintId\": 1}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.title").exists());

        long projectId = createProject(createTeam("Dates"), "Dates");
        call(post("/sprints/" + projectId), "{\"name\": \"Backwards\", \"startDate\": \"2026-10-14\", \"endDate\": \"2026-10-01\"}")
                .andExpect(status().isBadRequest());

        call(put("/tasks/1/status"), "{\"status\": \"FINISHED\"}").andExpect(status().isBadRequest());
    }

    @Test
    void emailsAreUnique() throws Exception {
        String email = unique() + "@example.com";
        String body = "{\"username\": \"a\", \"fullName\": \"A\", \"email\": \"" + email + "\"}";
        call(post("/users"), body).andExpect(status().isCreated());
        call(post("/users"), body).andExpect(status().isConflict());
    }

    // ---- helpers ------------------------------------------------------------

    private long createUser(String fullName) throws Exception {
        return id(call(post("/users"), """
                {"username": "%s", "fullName": "%s", "email": "%s@example.com", "password": "secret", "role": "Developer"}
                """.formatted(unique(), fullName, unique())));
    }

    private long createTeam(String name) throws Exception {
        return id(call(post("/teams"), "{\"name\": \"" + name + "\"}"));
    }

    private long createProject(long teamId, String name) throws Exception {
        return id(call(post("/projects/" + teamId), "{\"name\": \"" + name + "\", \"description\": \"test\"}"));
    }

    private long createSprint(long projectId, String start, String end) throws Exception {
        return id(call(post("/sprints/" + projectId), """
                {"name": "Sprint", "startDate": "%s", "endDate": "%s"}
                """.formatted(start, end)));
    }

    private ResultActions call(MockHttpServletRequestBuilder request) throws Exception {
        return mvc.perform(request);
    }

    private ResultActions call(MockHttpServletRequestBuilder request,
                               String body) throws Exception {
        return mvc.perform(request.contentType(MediaType.APPLICATION_JSON).content(body));
    }

    private JsonNode body(ResultActions result) throws Exception {
        return json.readTree(result.andReturn().getResponse().getContentAsString());
    }

    private long id(ResultActions result) throws Exception {
        return body(result.andExpect(status().is2xxSuccessful())).get("id").asLong();
    }

    private static String unique() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
