package com.example.projectmanager.service.impliments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projectmanager.persistance.dao.SprintRepository;
import com.example.projectmanager.persistance.entities.Sprint;
import com.example.projectmanager.service.interfaces.ISprint;

import java.util.List;

@Service
public class SprintImpl implements ISprint {

    @Autowired
    private SprintRepository sprintRepository;

    @Override
    public void addSprint(Sprint sprint) {
        sprintRepository.save(sprint);
    }

    @Override
    public void updateSprint(Long id, Sprint sprint) {
        Sprint existingSprint = sprintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + id));
        existingSprint.setName(sprint.getName());
        existingSprint.setStartDate(sprint.getStartDate());
        existingSprint.setEndDate(sprint.getEndDate());
        existingSprint.setProject(sprint.getProject());
        sprintRepository.save(existingSprint);
    }

    @Override
    public void deleteSprint(Long id) {
        sprintRepository.deleteById(id);
    }

    @Override
    public Sprint getSprintById(Long id) {
        return sprintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + id));
    }

    @Override
    public List<Sprint> getAllSprints() {
        return sprintRepository.findAll();
    }

    @Override
    public List<Sprint> getSprintsByProject(Long projectId) {
        return sprintRepository.findByProjectId(projectId);
    }
}

