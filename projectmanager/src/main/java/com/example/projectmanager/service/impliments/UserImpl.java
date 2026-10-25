package com.example.projectmanager.service.impliments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.projectmanager.exception.NotFoundException;
import com.example.projectmanager.persistance.dao.TaskRepository;
import com.example.projectmanager.persistance.dao.UserRepository;
import com.example.projectmanager.persistance.entities.Task;
import com.example.projectmanager.persistance.entities.Team;
import com.example.projectmanager.persistance.entities.User;
import com.example.projectmanager.service.interfaces.IUser;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserImpl implements IUser {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public User addUser(User user) {
        if (userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new IllegalStateException("Someone is already registered with " + user.getEmail());
        }
        user.setId(null);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, User changes) {
        User user = getUserById(id);
        if (userRepository.existsByEmailIgnoreCaseAndIdNot(changes.getEmail(), id)) {
            throw new IllegalStateException("Someone else is already registered with " + changes.getEmail());
        }
        user.setUsername(changes.getUsername());
        user.setFullName(changes.getFullName());
        user.setEmail(changes.getEmail());
        user.setRole(changes.getRole());
        // The password is never sent to the client, so an edit form comes back
        // without one. An empty value means "keep the current password".
        if (changes.getPassword() != null && !changes.getPassword().isBlank()) {
            user.setPassword(changes.getPassword());
        }
        return user;
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id);

        // Foreign keys from tasks and team memberships would otherwise block the delete.
        for (Task task : taskRepository.findByAssignedTo_Id(id)) {
            task.setAssignedTo(null);
        }
        for (Team team : new ArrayList<>(user.getTeams())) {
            team.getUsers().remove(user);
        }
        user.getTeams().clear();
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
