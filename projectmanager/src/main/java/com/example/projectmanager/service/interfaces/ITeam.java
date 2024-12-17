package com.example.projectmanager.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.example.projectmanager.persistance.entities.Team;

public interface ITeam {
    void addTeam(Team team);
    void updateTeam(Long id, Team team);
    void deleteTeam(Long id);
    Optional<Team> getTeamById(Long id);
    List<Team> getAllTeams();
}

