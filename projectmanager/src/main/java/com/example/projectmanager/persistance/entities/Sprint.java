package com.example.projectmanager.persistance.entities;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sprints")
public class Sprint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Nom du sprint

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate; // Date de début du sprint

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate; // Date de fin du sprint

    @ManyToOne
    private Project project; // Projet auquel le sprint est associé

    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL)
    private List<Task> tasks; // Liste des tâches associées au sprint

    // Constructeurs
    public Sprint() {
    }

    public Sprint(String name, Date startDate, Date endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters et Setters
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
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

