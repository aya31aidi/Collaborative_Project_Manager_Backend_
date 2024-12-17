package com.example.projectmanager.service.impliments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projectmanager.enums.TaskStatus;
import com.example.projectmanager.persistance.dao.SprintRepository;
import com.example.projectmanager.persistance.dao.TagRepository;
import com.example.projectmanager.persistance.dao.TaskRepository;
import com.example.projectmanager.persistance.dao.UserRepository;
import com.example.projectmanager.persistance.entities.Sprint;
import com.example.projectmanager.persistance.entities.Tag;
import com.example.projectmanager.persistance.entities.Task;
import com.example.projectmanager.persistance.entities.User;
import com.example.projectmanager.service.interfaces.ITask;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskImpl implements ITask {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SprintRepository sprintRepository;
    @Autowired
    private TagRepository tagRepository;
    @Override
    public Task addTask(Task task) {
        // Récupérer l'utilisateur assigné par ID
        User assignedUser = userRepository.findById(task.getAssignedTo().getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + task.getAssignedTo().getId()));
        task.setAssignedTo(assignedUser); // Assigner l'utilisateur à la tâche

        // Récupérer le sprint par ID
        Sprint sprint = sprintRepository.findById(task.getSprint().getId())
                .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + task.getSprint().getId()));
        task.setSprint(sprint); // Assigner le sprint à la tâche

        // Gérer la liste des tags
        if (task.getTags() != null && !task.getTags().isEmpty()) {
            List<Tag> tags = new ArrayList<>();
            for (Tag tag : task.getTags()) {
                // Rechercher si le tag existe déjà par son nom
                Optional<Tag> existingTagOpt = tagRepository.findByName(tag.getName());
                Tag tagToAdd;

                if (existingTagOpt.isPresent()) {
                    tagToAdd = existingTagOpt.get(); // Si le tag existe, réutiliser l'existant
                } else {
                    // Si le tag n'existe pas, le créer
                    Tag newTag = new Tag();
                    newTag.setName(tag.getName());
                    tagToAdd = tagRepository.save(newTag);
                }

                // Ajouter le tag à la liste des tags
                tags.add(tagToAdd);
            }

            // Associer la liste des tags à la tâche
            task.setTags(tags);
        }

        // Sauvegarder la tâche avec les tags associés
        return taskRepository.save(task);
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

