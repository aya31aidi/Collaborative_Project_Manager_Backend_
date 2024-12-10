package com.example.projectmanager.service.interfaces;


import java.util.List;

import com.example.projectmanager.persistance.entities.Task;

public interface ITask {
    void addTask(Task task);
    void updateTask(Long id, Task task);
    void deleteTask(Long id);
    Task getTaskById(Long id);
    List<Task> getAllTasks();
    List<Task> getTasksByProject(Long projectId);
}

