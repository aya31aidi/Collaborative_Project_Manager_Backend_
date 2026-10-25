package com.example.projectmanager.persistance.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "teams")
public class Team implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A team needs a name")
    @Size(max = 255)
    private String name;

    // Members are managed through /teams/{id}/addUser/{userId} and
    // /teams/{id}/users/{userId}, so renaming a team never touches them.
    @ManyToMany
    @JsonIgnoreProperties("teams")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<User> users = new ArrayList<>();

    // No cascade: deleting a team leaves its project in place, without a team.
    @OneToOne(mappedBy = "team")
    @JsonIgnoreProperties({"team", "sprints"})
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Project project;

    public Team() {
    }

    public Team(String name) {
        this.name = name;
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", project=" + (project != null ? project.getName() : "null") +
                '}';
    }
}
