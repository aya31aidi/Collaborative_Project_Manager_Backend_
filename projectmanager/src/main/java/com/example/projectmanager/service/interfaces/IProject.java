package com.example.projectmanager.service.interfaces;


import java.util.List;

import com.example.projectmanager.persistance.entities.Project;

public interface IProject {
    void addProject(Project project);
    void updateProject(Long id, Project project);
    void deleteProject(Long id);
    Project getProjectById(Long id);
    List<Project> getAllProjects();
}

