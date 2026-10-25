package com.example.projectmanager.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.projectmanager.persistance.entities.Project;
import com.example.projectmanager.persistance.entities.Sprint;
import com.example.projectmanager.persistance.entities.Task;
import com.example.projectmanager.service.interfaces.IProject;
import com.example.projectmanager.service.interfaces.ISprint;
import com.example.projectmanager.service.interfaces.ITask;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private IProject projectService;
    @Autowired
    private ISprint sprintService;
    @Autowired
    private ITask taskService;

    @PostMapping("/{teamId}")
    public ResponseEntity<Project> addProject(@PathVariable Long teamId, @Valid @RequestBody Project project) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.addProject(teamId, project));
    }

    @PutMapping("/{id}")
    public Project updateProject(@PathVariable Long id, @Valid @RequestBody Project project) {
        return projectService.updateProject(id, project);
    }

    @GetMapping("/{id}")
    public Project getProjectById(@PathVariable Long id) {
        return projectService.getProjectById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/team/{teamId}")
    public Project getProjectByTeamId(@PathVariable Long teamId) {
        return projectService.getProjectByTeamId(teamId);
    }

    @GetMapping("/{id}/sprints")
    public List<Sprint> getSprints(@PathVariable Long id) {
        projectService.getProjectById(id);
        return sprintService.getSprintsByProject(id);
    }

    @GetMapping("/{id}/tasks")
    public List<Task> getTasks(@PathVariable Long id) {
        projectService.getProjectById(id);
        return taskService.getTasksByProject(id);
    }
}
