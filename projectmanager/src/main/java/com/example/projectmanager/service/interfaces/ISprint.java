package com.example.projectmanager.service.interfaces;

import java.util.List;

import com.example.projectmanager.persistance.entities.Sprint;
import com.example.projectmanager.persistance.entities.User;

public interface ISprint {
    Sprint addSprint(Long projectId, Sprint sprint);

    Sprint updateSprint(Long id, Sprint sprint);

    void deleteSprint(Long id);

    Sprint getSprintById(Long id);

    List<Sprint> getAllSprints();

    List<Sprint> getSprintsByProject(Long projectId);

    List<User> getSprintMembers(Long id);
}
