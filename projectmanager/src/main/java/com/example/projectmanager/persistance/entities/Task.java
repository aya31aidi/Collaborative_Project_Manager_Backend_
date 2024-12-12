package com.example.projectmanager.persistance.entities;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

import com.example.projectmanager.enums.TaskPriority;
import com.example.projectmanager.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "tasks")
public class Task implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // Titre de la tâche

    @Column
    private String description; // Description de la tâche

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskStatus status; // Statut de la tâche (ENUM)

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskPriority priority; 

    @ManyToOne
    @JsonBackReference
    private Sprint sprint; // Sprint auquel appartient la tâche

    @ManyToOne
    private User assignedTo; // Utilisateur assigné à la tâche

    @ManyToMany
    @JsonManagedReference
    private List<Tag> tags; // Étiquettes associées à la tâche

    // Constructeurs
    public Task() {
    }

    public Task(String title, String description, TaskStatus status, TaskPriority priority) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    // Méthode utilitaire (facultative)
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                '}';
    }
}

