package com.example.projectmanager.service.interfaces;

import java.util.List;

import com.example.projectmanager.dto.TaskRequest;
import com.example.projectmanager.enums.TaskStatus;
import com.example.projectmanager.persistance.entities.Task;

public interface ITask {
    Task addTask(TaskRequest request);

    Task updateTask(Long id, TaskRequest request);

    Task updateTaskStatus(Long id, TaskStatus status);

    void deleteTask(Long id);

    Task getTaskById(Long id);

    List<Task> getAllTasks();

    List<Task> getTasksByProject(Long projectId);
}
