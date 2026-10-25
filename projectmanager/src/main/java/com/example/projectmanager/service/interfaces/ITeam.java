package com.example.projectmanager.service.interfaces;

import java.util.List;

import com.example.projectmanager.persistance.entities.Team;
import com.example.projectmanager.persistance.entities.User;

public interface ITeam {
    Team addTeam(Team team);

    Team updateTeam(Long id, Team team);

    void deleteTeam(Long id);

    Team getTeamById(Long id);

    List<Team> getAllTeams();

    List<User> getTeamMembers(Long teamId);

    Team addUserToTeam(Long teamId, Long userId);

    Team deleteUserFromTeam(Long teamId, Long userId);
}
