package com.example.projectmanager.service.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.projectmanager.persistance.entities.Project;
import com.example.projectmanager.persistance.entities.User;
import com.example.projectmanager.service.interfaces.IProject;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private IProject projectService;

    @PostMapping
    public void createProject(@RequestBody Project project) {
        projectService.addProject(project);
    }

    @PutMapping("/{id}")
    public void updateTag(@PathVariable Long id, @RequestBody Project project) {
        projectService.updateProject(id, project);
    }
    
    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }
    
    @GetMapping("/team/{teamId}")
    public ResponseEntity<Project> getProjectByTeamId(@PathVariable("teamId") Long teamId) {
        Project project = projectService.getProjectByTeamId(teamId);
        if (project != null) {
            return ResponseEntity.ok(project);
        }
        return ResponseEntity.notFound().build();
    }
}

