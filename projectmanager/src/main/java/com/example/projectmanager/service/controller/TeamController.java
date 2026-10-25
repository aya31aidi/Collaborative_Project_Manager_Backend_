package com.example.projectmanager.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.projectmanager.persistance.entities.Team;
import com.example.projectmanager.persistance.entities.User;
import com.example.projectmanager.service.interfaces.ITeam;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private ITeam teamService;

    @PostMapping
    public ResponseEntity<Team> createTeam(@Valid @RequestBody Team team) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.addTeam(team));
    }

    @PutMapping("/{id}")
    public Team updateTeam(@PathVariable Long id, @Valid @RequestBody Team team) {
        return teamService.updateTeam(id, team);
    }

    @GetMapping("/{id}")
    public Team getTeamById(@PathVariable Long id) {
        return teamService.getTeamById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Team> getAllTeams() {
        return teamService.getAllTeams();
    }

    @GetMapping("/{id}/users")
    public List<User> getUsersByTeamId(@PathVariable Long id) {
        return teamService.getTeamMembers(id);
    }

    @PostMapping("/{teamId}/addUser/{userId}")
    public Team addUserToTeam(@PathVariable Long teamId, @PathVariable Long userId) {
        return teamService.addUserToTeam(teamId, userId);
    }

    @DeleteMapping("/{teamId}/users/{userId}")
    public Team deleteUserFromTeam(@PathVariable Long teamId, @PathVariable Long userId) {
        return teamService.deleteUserFromTeam(teamId, userId);
    }
}
