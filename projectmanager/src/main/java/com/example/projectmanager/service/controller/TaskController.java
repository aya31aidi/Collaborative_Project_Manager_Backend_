package com.example.projectmanager.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.projectmanager.enums.TaskStatus;
import com.example.projectmanager.persistance.entities.Task;
import com.example.projectmanager.persistance.entities.User;
import com.example.projectmanager.service.interfaces.ITask;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITask taskService;

   
    @PostMapping()
    public void createTask(@RequestBody Task task) {
        taskService.addTask(task);
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
    public ResponseEntity<String> updateTaskStatus(@PathVariable Long id, @RequestBody Map<String, String> statusUpdate) {
        String newStatusString = statusUpdate.get("status");
        try {
            TaskStatus newStatus = TaskStatus.valueOf(newStatusString); // Convertit la chaîne en enum
            boolean isUpdated = taskService.updateTaskStatus(id, newStatus);

            if (isUpdated) {
                return ResponseEntity.ok("Task status updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status value");
        }
    }
}
