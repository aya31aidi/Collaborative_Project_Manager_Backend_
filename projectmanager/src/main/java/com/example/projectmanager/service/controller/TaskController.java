package com.example.projectmanager.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.projectmanager.dto.TaskRequest;
import com.example.projectmanager.enums.TaskStatus;
import com.example.projectmanager.persistance.entities.Task;
import com.example.projectmanager.service.interfaces.ITask;

import jakarta.validation.Valid;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITask taskService;

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.addTask(request));
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        return taskService.updateTask(id, request);
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    // Used by the board when a card is dropped in another column: {"status": "IN_PROGRESS"}
    @PutMapping("/{id}/status")
    public Task updateTaskStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return taskService.updateTaskStatus(id, parseStatus(body.get("status")));
    }

    private static TaskStatus parseStatus(String value) {
        try {
            return TaskStatus.valueOf(value);
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException(
                    "Unknown status \"" + value + "\", expected one of " + Arrays.toString(TaskStatus.values()));
        }
    }
}
