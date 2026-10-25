package com.example.projectmanager.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.projectmanager.persistance.entities.Sprint;
import com.example.projectmanager.persistance.entities.User;
import com.example.projectmanager.service.interfaces.ISprint;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/sprints")
public class SprintController {

    @Autowired
    private ISprint sprintService;

    @PostMapping("/{projectId}")
    public ResponseEntity<Sprint> addSprint(@PathVariable Long projectId, @Valid @RequestBody Sprint sprint) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sprintService.addSprint(projectId, sprint));
    }

    @PutMapping("/{id}")
    public Sprint updateSprint(@PathVariable Long id, @Valid @RequestBody Sprint sprint) {
        return sprintService.updateSprint(id, sprint);
    }

    @GetMapping("/{id}")
    public Sprint getSprintById(@PathVariable Long id) {
        return sprintService.getSprintById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSprint(@PathVariable Long id) {
        sprintService.deleteSprint(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Sprint> getAllSprints() {
        return sprintService.getAllSprints();
    }

    @GetMapping("/{id}/users")
    public List<User> getUsersForSprint(@PathVariable Long id) {
        return sprintService.getSprintMembers(id);
    }
}
