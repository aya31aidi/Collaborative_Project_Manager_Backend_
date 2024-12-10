package com.example.projectmanager.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.projectmanager.persistance.entities.Sprint;
import com.example.projectmanager.service.interfaces.ISprint;

import java.util.List;

@RestController
@RequestMapping("/sprints")
public class SprintController {

    @Autowired
    private ISprint sprintService;

    @PostMapping
    public void createSprint(@RequestBody Sprint sprint) {
        sprintService.addSprint(sprint);
    }

    @GetMapping("/{id}")
    public Sprint getSprintById(@PathVariable Long id) {
        return sprintService.getSprintById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteSprint(@PathVariable Long id) {
        sprintService.deleteSprint(id);
    }

    @GetMapping
    public List<Sprint> getAllSprints() {
        return sprintService.getAllSprints();
    }
}

