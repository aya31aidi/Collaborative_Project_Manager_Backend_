package com.example.projectmanager.persistance.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.projectmanager.persistance.entities.Tag;
import com.example.projectmanager.persistance.entities.Task;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
	Optional<Tag> findByName(String name);
	Tag findByTask (Task task);
	 @Query("SELECT t.tags FROM Task t WHERE t.id = :taskId")
	    List<Tag> findTagsByTaskId(@Param("taskId") Long taskId);
	  List<Tag> findByTaskId(Long taskId);
}

