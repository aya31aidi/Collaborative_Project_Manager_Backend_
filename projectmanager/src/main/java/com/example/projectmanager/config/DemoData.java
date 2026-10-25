package com.example.projectmanager.config;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectmanager.enums.TaskPriority;
import com.example.projectmanager.enums.TaskStatus;
import com.example.projectmanager.persistance.dao.ProjectRepository;
import com.example.projectmanager.persistance.dao.TeamRepository;
import com.example.projectmanager.persistance.dao.UserRepository;
import com.example.projectmanager.persistance.entities.Project;
import com.example.projectmanager.persistance.entities.Sprint;
import com.example.projectmanager.persistance.entities.Tag;
import com.example.projectmanager.persistance.entities.Task;
import com.example.projectmanager.persistance.entities.Team;
import com.example.projectmanager.persistance.entities.User;

/**
 * A small example workspace so a fresh install opens on something to look at:
 * two teams, two projects, and sprints dated around today. It only runs on an
 * empty database and can be switched off with app.demo-data=false.
 */
@Component
@ConditionalOnProperty(name = "app.demo-data", havingValue = "true")
public class DemoData implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoData.class);

    private final UserRepository users;
    private final TeamRepository teams;
    private final ProjectRepository projects;

    public DemoData(UserRepository users, TeamRepository teams, ProjectRepository projects) {
        this.users = users;
        this.teams = teams;
        this.projects = projects;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (users.count() > 0 || teams.count() > 0) {
            return;
        }

        User lena = user("lena.k", "Lena Krüger", "lena.krueger@example.com", "Product owner");
        User youssef = user("youssef.b", "Youssef Ben Ali", "youssef.benali@example.com", "Backend developer");
        User mara = user("mara.s", "Mara Schmidt", "mara.schmidt@example.com", "Frontend developer");
        User jonas = user("jonas.w", "Jonas Weber", "jonas.weber@example.com", "QA engineer");
        User salma = user("salma.h", "Salma Haddad", "salma.haddad@example.com", "UX designer");
        User tim = user("tim.n", "Tim Neumann", "tim.neumann@example.com", "Data engineer");

        Team web = team("Web Platform", lena, youssef, mara, jonas, salma);
        Team data = team("Data & Insights", youssef, tim, jonas);

        LocalDate today = LocalDate.now();

        Project portal = project("Customer Portal", "New self-service portal where customers follow orders, invoices and support requests.", web);
        Sprint discovery = sprint(portal, "Sprint 1 · Discovery", today.minusDays(30), today.minusDays(17));
        task(discovery, "Interview five key customers", "Collect the top pain points of the current portal.", TaskStatus.DONE, TaskPriority.HIGH, salma, "research");
        task(discovery, "Define the MVP scope", "One page, agreed with sales and support.", TaskStatus.DONE, TaskPriority.HIGH, lena, "planning");
        task(discovery, "Choose the auth provider", "Compare Keycloak and Auth0 for SSO.", TaskStatus.DONE, TaskPriority.MEDIUM, youssef, "backend", "security");

        Sprint build = sprint(portal, "Sprint 2 · Orders and invoices", today.minusDays(6), today.plusDays(8));
        task(build, "Orders REST endpoints", "Paged list, details and status history.", TaskStatus.DONE, TaskPriority.HIGH, youssef, "backend", "api");
        task(build, "Order list screen", "Filters by status and date, empty state included.", TaskStatus.IN_PROGRESS, TaskPriority.HIGH, mara, "frontend");
        task(build, "Invoice PDF download", "Generated on the server, cached for a day.", TaskStatus.IN_PROGRESS, TaskPriority.MEDIUM, youssef, "backend");
        task(build, "Design system tokens", "Colours, spacing and type scale shared with marketing.", TaskStatus.DONE, TaskPriority.MEDIUM, salma, "design");
        task(build, "End-to-end tests for login", "Happy path and locked account.", TaskStatus.TO_DO, TaskPriority.MEDIUM, jonas, "testing");
        task(build, "Accessibility review", "Keyboard navigation and contrast on the new screens.", TaskStatus.TO_DO, TaskPriority.LOW, salma, "design", "a11y");
        task(build, "Support ticket widget", "Embed the ticket form on every page.", TaskStatus.TO_DO, TaskPriority.LOW, mara, "frontend");

        sprint(portal, "Sprint 3 · Support centre", today.plusDays(9), today.plusDays(22));

        Project warehouse = project("Sales Data Warehouse", "Move the nightly sales reports from spreadsheets to a proper warehouse with dashboards.", data);
        Sprint pipeline = sprint(warehouse, "Sprint 1 · Ingestion", today.minusDays(4), today.plusDays(10));
        task(pipeline, "Nightly import from the ERP", "Incremental load, retries on failure.", TaskStatus.IN_PROGRESS, TaskPriority.HIGH, tim, "pipeline");
        task(pipeline, "Star schema for sales", "Facts: order lines. Dimensions: customer, product, date.", TaskStatus.DONE, TaskPriority.HIGH, tim, "modelling");
        task(pipeline, "Data quality checks", "Row counts and null checks after every load.", TaskStatus.TO_DO, TaskPriority.MEDIUM, jonas, "testing");
        task(pipeline, "Expose KPIs through the API", "Revenue per week and per region.", TaskStatus.TO_DO, TaskPriority.MEDIUM, youssef, "api");

        projects.saveAll(List.of(portal, warehouse));
        log.info("Demo workspace created: {} people, 2 teams, 2 projects", users.count());
    }

    private User user(String username, String fullName, String email, String role) {
        return users.save(new User(username, fullName, email, null, role));
    }

    private Team team(String name, User... members) {
        Team team = new Team(name);
        for (User member : members) {
            team.getUsers().add(member);
            member.getTeams().add(team);
        }
        return teams.save(team);
    }

    private static Project project(String name, String description, Team team) {
        Project project = new Project(name, description);
        project.setTeam(team);
        team.setProject(project);
        return project;
    }

    private static Sprint sprint(Project project, String name, LocalDate start, LocalDate end) {
        Sprint sprint = new Sprint(name, start, end);
        sprint.setProject(project);
        project.getSprints().add(sprint);
        return sprint;
    }

    private static void task(Sprint sprint, String title, String description, TaskStatus status,
                             TaskPriority priority, User assignee, String... tags) {
        Task task = new Task(title, description, status, priority);
        task.setSprint(sprint);
        task.setAssignedTo(assignee);
        for (String tag : tags) {
            task.getTags().add(new Tag(tag, task));
        }
        sprint.getTasks().add(task);
    }
}
