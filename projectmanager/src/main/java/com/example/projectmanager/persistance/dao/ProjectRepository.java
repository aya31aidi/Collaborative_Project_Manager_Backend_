package com.example.projectmanager.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.projectmanager.persistance.entities.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
	@Query("SELECT p FROM Project p WHERE p.team.id = :teamId")
    Project findProjectByTeamId(@Param("teamId") Long teamId);
	
}

