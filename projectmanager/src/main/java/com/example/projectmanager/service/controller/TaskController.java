package com.example.projectmanager.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.projectmanager.enums.TaskStatus;
import com.example.projectmanager.persistance.dao.ProjectRepository;
import com.example.projectmanager.persistance.dao.SprintRepository;
import com.example.projectmanager.persistance.entities.Task;
import com.example.projectmanager.persistance.entities.User;
import com.example.projectmanager.service.interfaces.ITask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITask taskService;

   
    @PostMapping("")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task savedTask = taskService.addTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    
    @PutMapping("/{id}")
    public void updateTag(@PathVariable Long id, @RequestBody Task task) {
        taskService.updateTask(id, task);
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, String>> updateTaskStatus(@PathVariable Long id, @RequestBody Map<String, String> statusUpdate) {
        String newStatusString = statusUpdate.get("status");
        try {
            TaskStatus newStatus = TaskStatus.valueOf(newStatusString); // Convert the string to enum
            boolean isUpdated = taskService.updateTaskStatus(id, newStatus);

            if (isUpdated) {
                // Return a JSON response with a success message
                Map<String, String> response = new HashMap<>();
                response.put("message", "Task status updated successfully");
                return ResponseEntity.ok(response);
            } else {
                // Return a JSON response with an error message
                Map<String, String> response = new HashMap<>();
                response.put("message", "Task not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (IllegalArgumentException e) {
            // Return a JSON response with an error message
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid status value");
            return ResponseEntity.badRequest().body(response);
        }
    }

}
