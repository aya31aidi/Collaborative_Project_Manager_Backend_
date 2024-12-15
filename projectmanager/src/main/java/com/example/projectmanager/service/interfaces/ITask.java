package com.example.projectmanager.service.interfaces;


import java.util.List;

import com.example.projectmanager.enums.TaskStatus;
import com.example.projectmanager.persistance.entities.Task;

public interface ITask {
    Task addTask(Task task);
    void updateTask(Long id, Task task);
    void deleteTask(Long id);
    Task getTaskById(Long id);
    List<Task> getAllTasks();
    List<Task> getTasksByProject(Long projectId);
	boolean updateTaskStatus(Long id, TaskStatus newStatus);
}

