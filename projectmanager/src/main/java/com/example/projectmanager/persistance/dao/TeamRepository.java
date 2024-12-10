package com.example.projectmanager.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectmanager.persistance.entities.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
    // Ajoute ici des méthodes spécifiques si nécessaire
}

