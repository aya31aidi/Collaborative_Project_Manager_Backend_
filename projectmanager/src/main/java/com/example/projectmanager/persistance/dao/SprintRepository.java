package com.example.projectmanager.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectmanager.persistance.entities.Sprint;

import java.util.List;

public interface SprintRepository extends JpaRepository<Sprint, Long> {
    List<Sprint> findByProjectId(Long projectId); // Récupérer les sprints d'un projet spécifique
}

