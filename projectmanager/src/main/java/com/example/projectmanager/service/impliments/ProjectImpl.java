package com.example.projectmanager.service.impliments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectmanager.exception.NotFoundException;
import com.example.projectmanager.persistance.dao.ProjectRepository;
import com.example.projectmanager.persistance.dao.TeamRepository;
import com.example.projectmanager.persistance.entities.Project;
import com.example.projectmanager.persistance.entities.Team;
import com.example.projectmanager.service.interfaces.IProject;

import java.util.List;

@Service
@Transactional
public class ProjectImpl implements IProject {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TeamRepository teamRepository;

    @Override
    public Project addProject(Long teamId, Project project) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NotFoundException("Team", teamId));

        // projects.team_id is unique: a team works on a single project.
        projectRepository.findByTeamId(teamId).ifPresent(existing -> {
            throw new IllegalStateException(
                    "The team \"" + team.getName() + "\" already works on \"" + existing.getName() + "\"");
        });

        project.setId(null);
        project.setTeam(team);
        team.setProject(project);
        return projectRepository.save(project);
    }

    @Override
    public Project updateProject(Long id, Project changes) {
        // Only the editable fields. The team and the sprints have their own
        // endpoints, and copying them from the body used to wipe them out.
        Project project = getProjectById(id);
        project.setName(changes.getName());
        project.setDescription(changes.getDescription());
        return project;
    }

    @Override
    public void deleteProject(Long id) {
        Project project = getProjectById(id);
        if (project.getTeam() != null) {
            project.getTeam().setProject(null);
        }
        // Sprints, their tasks and the tasks' tags go with it (cascade).
        projectRepository.delete(project);
    }

    @Override
    @Transactional(readOnly = true)
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Project getProjectByTeamId(Long teamId) {
        return projectRepository.findByTeamId(teamId)
                .orElseThrow(() -> new NotFoundException("Project for team", teamId));
    }
}
