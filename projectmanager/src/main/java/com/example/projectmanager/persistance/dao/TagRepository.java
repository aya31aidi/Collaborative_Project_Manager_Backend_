package com.example.projectmanager.persistance.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectmanager.persistance.entities.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByTask_IdOrderByIdAsc(Long taskId);
}
