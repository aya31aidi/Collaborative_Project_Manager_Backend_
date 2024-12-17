package com.example.projectmanager.persistance.entities;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

import com.example.projectmanager.enums.TaskPriority;
import com.example.projectmanager.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "tasks")
public class Task implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("title")
    private String title; 
    @JsonProperty("description")
    private String description; 

    @Enumerated(EnumType.STRING)
    @JsonProperty("status")
    private TaskStatus status; 

    @Enumerated(EnumType.STRING)
    @JsonProperty("priority")
    private TaskPriority priority; 

    @ManyToOne
    @JsonProperty("sprint")
    @JsonBackReference
    @JoinColumn(name = "sprint_id", referencedColumnName = "id")
    private Sprint sprint; 

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User assignedTo; 

    @OneToMany(mappedBy = "task")
    private List<Tag> tags; 

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

