package com.example.projectmanager.service.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.projectmanager.persistance.entities.User;
import com.example.projectmanager.service.interfaces.IUser;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUser userService;

    @PostMapping
    public void createUser(@RequestBody User user) {
        userService.addUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}

