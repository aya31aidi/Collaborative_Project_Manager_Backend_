package com.example.projectmanager.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.projectmanager.persistance.entities.Tag;
import com.example.projectmanager.persistance.entities.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

	@Query("SELECT t.tags FROM Task t WHERE t.id = :taskId")
	List<Tag> findTagsByTaskId(@Param("taskId") Long taskId);

   
}

