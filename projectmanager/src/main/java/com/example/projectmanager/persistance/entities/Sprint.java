package com.example.projectmanager.persistance.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "sprints")
public class Sprint implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A sprint needs a name")
    @Size(max = 255)
    private String name;

    // Plain dates ("2026-10-20" in JSON): a sprint starts and ends on a day,
    // not at a moment in some time zone.
    @NotNull(message = "The start date is required")
    private LocalDate startDate;

    @NotNull(message = "The end date is required")
    private LocalDate endDate;

    // Only the id and name of the project go out with a sprint, which keeps
    // the JSON free of project -> sprints -> project loops.
    @ManyToOne
    @JsonIgnore
    private Project project;

    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Task> tasks = new ArrayList<>();

    public Sprint() {
    }

    public Sprint(String name, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @JsonProperty(value = "projectId", access = JsonProperty.Access.READ_ONLY)
    public Long getProjectId() {
        return project != null ? project.getId() : null;
    }

    @JsonProperty(value = "projectName", access = JsonProperty.Access.READ_ONLY)
    public String getProjectName() {
        return project != null ? project.getName() : null;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "Sprint{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", project=" + (project != null ? project.getName() : "null") +
                '}';
    }
}
