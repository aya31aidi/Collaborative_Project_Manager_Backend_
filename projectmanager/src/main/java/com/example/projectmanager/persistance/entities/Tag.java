package com.example.projectmanager.persistance.entities;

import jakarta.persistence.*;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "tags")
public class Tag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // A tag is a label on one task. The task comes back in a short form,
    // without its own tag list or assignee.
    @ManyToOne
    @JoinColumn(name = "task_id")
    @JsonIgnoreProperties({"tags", "assignedTo", "description"})
    private Task task;

    public Tag() {
    }

    public Tag(String name, Task task) {
        this.name = name;
        this.task = task;
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

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
