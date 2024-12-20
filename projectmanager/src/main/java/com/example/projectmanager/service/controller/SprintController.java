package com.example.projectmanager.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.projectmanager.persistance.entities.Sprint;
import com.example.projectmanager.persistance.entities.Team;
import com.example.projectmanager.persistance.entities.User;
import com.example.projectmanager.service.interfaces.ISprint;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sprints")
public class SprintController {

    @Autowired
    private ISprint sprintService;

    @PostMapping("/{projectId}")
    public ResponseEntity<Sprint> addSprint(@PathVariable Long projectId, @RequestBody Sprint sprint) {
        try {
            Sprint createdSprint = sprintService.addSprint(projectId, sprint); // Appeler le service pour ajouter le sprint
            return new ResponseEntity<>(createdSprint, HttpStatus.CREATED); // Retourner une réponse avec le sprint créé
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Retourner une erreur si le projet n'est pas trouvé
        }
    }

    @PutMapping("/{id}")
    public void updateTag(@PathVariable Long id, @RequestBody Sprint sprint) {
        sprintService.updateSprint(id, sprint);
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
    
    @GetMapping("/{id}/users")
    public List<User> getUsersForSprint(@PathVariable Long id) {
        Sprint sprint = sprintService.getSprintById(id); // Get the sprint by its ID
        List<User> users = new ArrayList<>();
        
        // Get the single team of the project and add its users
        Team team = sprint.getProject().getTeam();
        if (team != null) {
            users.addAll(team.getUsers());
        }
        
        return users;
    }

}

