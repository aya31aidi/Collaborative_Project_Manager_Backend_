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
        User assignedUser = userRepository.findById(task.getAssignedTo().getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + task.getAssignedTo().getId()));
        task.setAssignedTo(assignedUser); // Assigner l'utilisateur à la tâche

        Sprint sprint = sprintRepository.findById(task.getSprint().getId())
                .orElseThrow(() -> new RuntimeException("Sprint not found with id: " + task.getSprint().getId()));
        task.setSprint(sprint); // Assigner le sprint à la tâche

        if (task.getTags() != null && !task.getTags().isEmpty()) {
            List<Tag> tags = new ArrayList<>();
            for (Tag tag : task.getTags()) {
                Optional<Tag> existingTagOpt = tagRepository.findByName(tag.getName());
                Tag tagToAdd;

                if (existingTagOpt.isPresent()) {
                    tagToAdd = existingTagOpt.get(); // Si le tag existe, réutiliser l'existant
                } else {
                    Tag newTag = new Tag();
                    newTag.setName(tag.getName());
                    tagToAdd = tagRepository.save(newTag);
                }

                tags.add(tagToAdd);
            }

            task.setTags(tags);
        }

        return taskRepository.save(task);
    }




    @Override
    public Task updateTask(Long taskId, Task taskDetails) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        existingTask.setTitle(taskDetails.getTitle());
        existingTask.setDescription(taskDetails.getDescription());
        existingTask.setPriority(taskDetails.getPriority());
        existingTask.setStatus(taskDetails.getStatus());

        if (taskDetails.getAssignedTo() != null && taskDetails.getAssignedTo().getId() != null) {
            User assignedUser = userRepository.findById(taskDetails.getAssignedTo().getId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + taskDetails.getAssignedTo().getId()));
            existingTask.setAssignedTo(assignedUser);
        }


        if (taskDetails.getTags() != null && !taskDetails.getTags().isEmpty()) {
            List<Tag> tags = new ArrayList<>();
            for (Tag tag : taskDetails.getTags()) {
                Optional<Tag> existingTagOpt = tagRepository.findByName(tag.getName());
                Tag tagToAdd;

                if (existingTagOpt.isPresent()) {
                    tagToAdd = existingTagOpt.get(); 
                } else {
                    Tag newTag = new Tag();
                    newTag.setName(tag.getName());
                    tagToAdd = tagRepository.save(newTag);
                }

                tags.add(tagToAdd); 
            }

            existingTask.setTags(tags);
        } else {
            existingTask.setTags(new ArrayList<>());
        }

        return taskRepository.save(existingTask);
    }


    @Override
    public boolean deleteTask(Long taskId) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
            return true;
        } else {
            return false;
        }
    }
    
    public List<Tag> getTagsByTaskId(Long taskId) {
        return tagRepository.findByTaskId(taskId);
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

