package com.example.projectmanager.persistance.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.projectmanager.persistance.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); 
}

