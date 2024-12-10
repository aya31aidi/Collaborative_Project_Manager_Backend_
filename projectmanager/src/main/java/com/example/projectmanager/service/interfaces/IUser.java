package com.example.projectmanager.service.interfaces;

import java.util.List;

import com.example.projectmanager.persistance.entities.User;

public interface IUser {
    void addUser(User user);
    void updateUser(Long id, User user);
    void deleteUser(Long id);
    User getUserById(Long id);
    List<User> getAllUsers();
}

