package com.example.projectmanager.persistance.entities;
import jakarta.persistence.*;
import java.util.List;

import com.example.projectmanager.enums.TaskStatus;

@Entity
@Table(name = "tasks")
public class Task {

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
    private int priority; // Priorité (par exemple : 1 = Haute, 2 = Moyenne, 3 = Basse)

    @ManyToOne
    private Sprint sprint; // Sprint auquel appartient la tâche

    @ManyToOne
    private User assignedTo; // Utilisateur assigné à la tâche

    @ManyToMany
    private List<Tag> tags; // Étiquettes associées à la tâche

    // Constructeurs
    public Task() {
    }

    public Task(String title, String description, TaskStatus status, int priority) {
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
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

