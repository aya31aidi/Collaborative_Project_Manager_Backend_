package com.example.projectmanager.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectmanager.persistance.entities.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findBySprint_Project_IdOrderByIdAsc(Long projectId);

    List<Task> findByAssignedTo_Id(Long userId);
}
