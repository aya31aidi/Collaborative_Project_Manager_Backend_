package com.example.projectmanager.service.impliments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectmanager.dto.TaskRequest;
import com.example.projectmanager.enums.TaskPriority;
import com.example.projectmanager.enums.TaskStatus;
import com.example.projectmanager.exception.NotFoundException;
import com.example.projectmanager.persistance.dao.SprintRepository;
import com.example.projectmanager.persistance.dao.TaskRepository;
import com.example.projectmanager.persistance.dao.UserRepository;
import com.example.projectmanager.persistance.entities.Sprint;
import com.example.projectmanager.persistance.entities.Tag;
import com.example.projectmanager.persistance.entities.Task;
import com.example.projectmanager.persistance.entities.User;
import com.example.projectmanager.service.interfaces.ITask;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskImpl implements ITask {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SprintRepository sprintRepository;

    @Override
    public Task addTask(TaskRequest request) {
        Task task = new Task();
        apply(task, request);
        if (request.tags() != null) {
            syncTags(task, request.tags());
        }
        task.getSprint().getTasks().add(task);
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, TaskRequest request) {
        Task task = getTaskById(id);
        Sprint previous = task.getSprint();
        apply(task, request);
        if (previous != null && previous != task.getSprint()) {
            previous.getTasks().remove(task);
            task.getSprint().getTasks().add(task);
        }
        // No "tags" in the body keeps the current ones; an empty list clears them.
        if (request.tags() != null) {
            syncTags(task, request.tags());
        }
        return task;
    }

    @Override
    public Task updateTaskStatus(Long id, TaskStatus status) {
        Task task = getTaskById(id);
        task.setStatus(status);
        return task;
    }

    @Override
    public void deleteTask(Long id) {
        Task task = getTaskById(id);
        // The cascade takes its tags along.
        if (task.getSprint() != null) {
            task.getSprint().getTasks().remove(task);
        }
        taskRepository.delete(task);
    }

    @Override
    @Transactional(readOnly = true)
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getTasksByProject(Long projectId) {
        return taskRepository.findBySprint_Project_IdOrderByIdAsc(projectId);
    }

    private void apply(Task task, TaskRequest request) {
        Sprint sprint = sprintRepository.findById(request.sprintId())
                .orElseThrow(() -> new NotFoundException("Sprint", request.sprintId()));
        User assignee = null;
        if (request.assigneeId() != null) {
            assignee = userRepository.findById(request.assigneeId())
                    .orElseThrow(() -> new NotFoundException("User", request.assigneeId()));
        }

        task.setTitle(request.title().trim());
        task.setDescription(request.description());
        task.setStatus(request.status() != null ? request.status() : TaskStatus.TO_DO);
        task.setPriority(request.priority() != null ? request.priority() : TaskPriority.MEDIUM);
        task.setSprint(sprint);
        task.setAssignedTo(assignee);
    }

    /**
     * Makes the task's tags match the given names, ignoring case and blanks.
     * Tags that stay keep their row, so their ids do not change on every save.
     */
    private static void syncTags(Task task, List<String> names) {
        Map<String, String> wanted = new LinkedHashMap<>();
        for (String name : names) {
            if (name != null && !name.isBlank()) {
                wanted.putIfAbsent(key(name), name.trim());
            }
        }

        task.getTags().removeIf(tag -> !wanted.containsKey(key(tag.getName())));
        Set<String> present = task.getTags().stream()
                .map(tag -> key(tag.getName()))
                .collect(Collectors.toSet());
        wanted.forEach((key, name) -> {
            if (!present.contains(key)) {
                task.getTags().add(new Tag(name, task));
            }
        });
    }

    private static String key(String name) {
        return name == null ? "" : name.trim().toLowerCase(Locale.ROOT);
    }
}
