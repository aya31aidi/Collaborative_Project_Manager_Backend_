package com.example.projectmanager.service.impliments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projectmanager.enums.TaskStatus;
import com.example.projectmanager.persistance.dao.TaskRepository;
import com.example.projectmanager.persistance.dao.UserRepository;
import com.example.projectmanager.persistance.entities.Task;
import com.example.projectmanager.persistance.entities.User;
import com.example.projectmanager.service.interfaces.ITask;

import java.util.List;
import java.util.Optional;

@Service
public class TaskImpl implements ITask {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public Task addTask(Task task) {
        // Récupérer l'utilisateur par ID et assigner à la tâche
        User assignedUser = userRepository.findById(task.getAssignedTo().getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + task.getAssignedTo().getId()));

        task.setAssignedTo(assignedUser);  // Assigner l'utilisateur à la tâche
        return taskRepository.save(task);  // Sauvegarder la tâche
    }

    @Override
    public void updateTask(Long id, Task task) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setPriority(task.getPriority());
        existingTask.setStatus(task.getStatus());
        existingTask.setSprint(task.getSprint());
        existingTask.setAssignedTo(task.getAssignedTo());
        taskRepository.save(existingTask);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

	@Override
	public List<Task> getTasksByProject(Long projectId) {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean updateTaskStatus(Long id, TaskStatus newStatus) {
	    Optional<Task> taskOpt = taskRepository.findById(id);
	    if (taskOpt.isPresent()) {
	        Task task = taskOpt.get();
	        task.setStatus(newStatus);
	        taskRepository.save(task);
	        return true;
	    }
	    return false;
	}

}

