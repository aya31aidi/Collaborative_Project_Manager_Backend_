package com.example.projectmanager.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectmanager.persistance.entities.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Ajoute ici des méthodes spécifiques si nécessaire
}

