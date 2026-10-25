package com.example.projectmanager.persistance.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectmanager.persistance.entities.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByTeamId(Long teamId);
}
