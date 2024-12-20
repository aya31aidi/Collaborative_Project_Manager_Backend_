package com.example.projectmanager.service.impliments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projectmanager.persistance.dao.ProjectRepository;
import com.example.projectmanager.persistance.dao.TeamRepository;
import com.example.projectmanager.persistance.entities.Project;
import com.example.projectmanager.persistance.entities.Team;
import com.example.projectmanager.service.interfaces.IProject;

import java.util.List;

@Service
public class ProjectImpl implements IProject {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TeamRepository teamRepository;
    
    @Override
    public Project addProject(Long teamId, Project project) {
        // Retrieve the team by its ID
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found"));

        // Set the team to the project
        project.setTeam(team);

        // Save the project to the database
        return projectRepository.save(project);
    }

    @Override
    public void updateProject(Long id, Project project) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        existingProject.setName(project.getName());
        existingProject.setDescription(project.getDescription());
        existingProject.setTeam(project.getTeam());
        existingProject.setSprints(project.getSprints());
        projectRepository.save(existingProject);
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
    public Project getProjectByTeamId(Long teamId) {
        return projectRepository.findProjectByTeamId(teamId);
    }
}
