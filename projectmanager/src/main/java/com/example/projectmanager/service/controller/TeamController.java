package com.example.projectmanager.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.projectmanager.persistance.entities.Team;
import com.example.projectmanager.persistance.entities.User;
import com.example.projectmanager.service.interfaces.ITeam;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teams")
public class TeamController {

    @Autowired
    private ITeam teamService;

    @PostMapping
    public void createTeam(@RequestBody Team team) {
        teamService.addTeam(team);
    }

    @PutMapping("/{id}")
    public void updateTag(@PathVariable Long id, @RequestBody Team team) {
        teamService.updateTeam(id, team);
    }
    @GetMapping("/{id}")
    public Optional<Team> getTeamById(@PathVariable Long id) {
        return teamService.getTeamById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
    }

    @GetMapping
    public List<Team> getAllTeams() {
        return teamService.getAllTeams();
    }
    
    @GetMapping("/{id}/users")
    public ResponseEntity<List<User>> getUsersByTeamId(@PathVariable Long id) {
    	 Optional<Team> team = teamService.getTeamById(id);
    	    if (team.isPresent()) {
    	        return ResponseEntity.ok(team.get().getUsers());
    	    } else {
    	        return ResponseEntity.notFound().build();
    	    }
}
}
