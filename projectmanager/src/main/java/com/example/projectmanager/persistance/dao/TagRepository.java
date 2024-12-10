package com.example.projectmanager.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.projectmanager.persistance.entities.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name); // Méthode pour récupérer un tag par son nom
}

