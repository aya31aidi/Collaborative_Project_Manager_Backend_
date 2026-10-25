package com.example.projectmanager.service.impliments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectmanager.exception.NotFoundException;
import com.example.projectmanager.persistance.dao.TeamRepository;
import com.example.projectmanager.persistance.dao.UserRepository;
import com.example.projectmanager.persistance.entities.Team;
import com.example.projectmanager.persistance.entities.User;
import com.example.projectmanager.service.interfaces.ITeam;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TeamImpl implements ITeam {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Team addTeam(Team team) {
        team.setId(null);
        return teamRepository.save(team);
    }

    @Override
    public Team updateTeam(Long id, Team changes) {
        // A rename only. Members are added and removed one by one.
        Team team = getTeamById(id);
        team.setName(changes.getName());
        return team;
    }

    @Override
    public void deleteTeam(Long id) {
        Team team = getTeamById(id);
        // The project stays and simply has no team any more.
        if (team.getProject() != null) {
            team.getProject().setTeam(null);
        }
        teamRepository.delete(team);
    }

    @Override
    @Transactional(readOnly = true)
    public Team getTeamById(Long id) {
        return teamRepository.findById(id).orElseThrow(() -> new NotFoundException("Team", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getTeamMembers(Long teamId) {
        return new ArrayList<>(getTeamById(teamId).getUsers());
    }

    @Override
    public Team addUserToTeam(Long teamId, Long userId) {
        Team team = getTeamById(teamId);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User", userId));

        boolean alreadyIn = team.getUsers().stream().anyMatch(member -> member.getId().equals(userId));
        if (!alreadyIn) {
            team.getUsers().add(user);
            user.getTeams().add(team);
        }
        return team;
    }

    @Override
    public Team deleteUserFromTeam(Long teamId, Long userId) {
        Team team = getTeamById(teamId);
        User member = team.getUsers().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Member of team " + teamId + ": user", userId));

        team.getUsers().remove(member);
        member.getTeams().remove(team);
        return team;
    }
}
