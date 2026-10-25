package com.example.projectmanager.service.impliments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectmanager.exception.NotFoundException;
import com.example.projectmanager.persistance.dao.ProjectRepository;
import com.example.projectmanager.persistance.dao.SprintRepository;
import com.example.projectmanager.persistance.entities.Project;
import com.example.projectmanager.persistance.entities.Sprint;
import com.example.projectmanager.persistance.entities.Team;
import com.example.projectmanager.persistance.entities.User;
import com.example.projectmanager.service.interfaces.ISprint;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SprintImpl implements ISprint {

    @Autowired
    private SprintRepository sprintRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Sprint addSprint(Long projectId, Sprint sprint) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project", projectId));
        checkDates(sprint);

        sprint.setId(null);
        sprint.setProject(project);
        project.getSprints().add(sprint);
        return sprintRepository.save(sprint);
    }

    @Override
    public Sprint updateSprint(Long id, Sprint changes) {
        checkDates(changes);
        Sprint sprint = getSprintById(id);
        sprint.setName(changes.getName());
        sprint.setStartDate(changes.getStartDate());
        sprint.setEndDate(changes.getEndDate());
        return sprint;
    }

    @Override
    public void deleteSprint(Long id) {
        Sprint sprint = getSprintById(id);
        // Its tasks (and their tags) go with it through the cascade.
        if (sprint.getProject() != null) {
            sprint.getProject().getSprints().remove(sprint);
        }
        sprintRepository.delete(sprint);
    }

    @Override
    @Transactional(readOnly = true)
    public Sprint getSprintById(Long id) {
        return sprintRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sprint", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sprint> getAllSprints() {
        return sprintRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Sprint> getSprintsByProject(Long projectId) {
        return sprintRepository.findByProject_IdOrderByStartDateAsc(projectId);
    }

    /** The people a task in this sprint can be assigned to: the project's team. */
    @Override
    @Transactional(readOnly = true)
    public List<User> getSprintMembers(Long id) {
        Project project = getSprintById(id).getProject();
        Team team = project != null ? project.getTeam() : null;
        return team == null ? List.of() : new ArrayList<>(team.getUsers());
    }

    private static void checkDates(Sprint sprint) {
        if (sprint.getStartDate() != null && sprint.getEndDate() != null
                && sprint.getEndDate().isBefore(sprint.getStartDate())) {
            throw new IllegalArgumentException("A sprint cannot end before it starts");
        }
    }
}
