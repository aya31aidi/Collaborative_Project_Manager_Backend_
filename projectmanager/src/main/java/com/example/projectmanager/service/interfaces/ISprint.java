package com.example.projectmanager.service.interfaces;

import java.util.List;

import com.example.projectmanager.persistance.entities.Sprint;

public interface ISprint {
    void updateSprint(Long id, Sprint sprint);
    void deleteSprint(Long id);
    Sprint getSprintById(Long id);
    List<Sprint> getAllSprints();
    List<Sprint> getSprintsByProject(Long projectId);
	Sprint addSprint(Long projectId, Sprint sprint);
}
