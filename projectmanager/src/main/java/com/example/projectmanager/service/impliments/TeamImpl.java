package com.example.projectmanager.service.impliments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projectmanager.persistance.dao.TeamRepository;
import com.example.projectmanager.persistance.entities.Team;
import com.example.projectmanager.service.interfaces.ITeam;

import java.util.List;

@Service
public class TeamImpl implements ITeam {

    @Autowired
    private TeamRepository teamRepository;

    @Override
    public void addTeam(Team team) {
        teamRepository.save(team);
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
    public Team getTeamById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found with id: " + id));
    }

    @Override
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }
}

