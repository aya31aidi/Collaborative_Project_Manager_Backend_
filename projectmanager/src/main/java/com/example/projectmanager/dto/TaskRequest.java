package com.example.projectmanager.dto;

import java.util.List;

import com.example.projectmanager.enums.TaskPriority;
import com.example.projectmanager.enums.TaskStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Body of POST /tasks and PUT /tasks/{id}. Relations travel as ids and tags as
 * plain names, so a client never has to send whole users or sprints back.
 * A missing status or priority falls back to TO_DO / MEDIUM; a missing
 * assignee leaves the task unassigned.
 */
public record TaskRequest(
        @NotBlank(message = "A task needs a title") @Size(max = 255) String title,
        @Size(max = 255, message = "The description is limited to 255 characters") String description,
        TaskStatus status,
        TaskPriority priority,
        @NotNull(message = "A task belongs to a sprint") Long sprintId,
        Long assigneeId,
        List<@Size(max = 40, message = "A tag is limited to 40 characters") String> tags) {
}
