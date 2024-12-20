package com.example.projectmanager.service.impliments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projectmanager.persistance.dao.TeamRepository;
import com.example.projectmanager.persistance.dao.UserRepository;
import com.example.projectmanager.persistance.entities.Team;
import com.example.projectmanager.persistance.entities.User;
import com.example.projectmanager.service.interfaces.ITeam;

import java.util.List;
import java.util.Optional;

@Service
public class TeamImpl implements ITeam {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired UserRepository userRepository;
    @Override
    public Team addTeam(Team team) {
        return teamRepository.save(team);
    }

    @Override
    public void updateTeam(Long id, Team team) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + id));
        existingTeam.setName(team.getName());
        existingTeam.setUsers(team.getUsers());
        existingTeam.setProject(team.getProject());
        teamRepository.save(existingTeam);
    }

    @Override
    public void deleteTeam(Long id) {
        teamRepository.deleteById(id);
    }

    @Override
    public Optional<Team> getTeamById(Long id) {
        return teamRepository.findById(id);  
    }

    @Override
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }
    @Override
    public void deleteUserFromTeam(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + teamId));
        
        User userToRemove = team.getUsers().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        team.getUsers().remove(userToRemove);

        teamRepository.save(team);
    }
    @Override
    public void addUserToTeam(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + teamId));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        team.getUsers().add(user);
        
        teamRepository.save(team);
    }

}

