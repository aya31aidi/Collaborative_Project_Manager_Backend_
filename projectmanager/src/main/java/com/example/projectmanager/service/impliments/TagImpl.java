package com.example.projectmanager.service.impliments;

import com.example.projectmanager.persistance.dao.TagRepository;
import com.example.projectmanager.persistance.dao.TaskRepository;
import com.example.projectmanager.persistance.entities.Tag;
import com.example.projectmanager.persistance.entities.Task;
import com.example.projectmanager.service.interfaces.ITag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagImpl implements ITag {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Override
    public void addTag(String tagName, Long taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found"));

        Tag tag = new Tag();
        tag.setName(tagName);
        tag.setTask(task);
        task.getTags().add(tag);

        tagRepository.save(tag);
        taskRepository.save(task);
    }

    @Override
    public void updateTag(Long id, Tag tag, Long idtask) {
    	System.out.print(idtask);
        Tag existingTag = tagRepository.findById(id).orElseThrow(() -> new RuntimeException("Tag not found"));
        
        existingTag.setName(tag.getName());
        
        Task task = taskRepository.findById(idtask)
                    .orElseThrow(() -> new RuntimeException("Task not found"));
        System.out.print(task);
        existingTag.setTask(task);
        
        tagRepository.save(existingTag);
    }



    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public Tag getTagById(Long id) {
        return tagRepository.findById(id).orElseThrow(() -> new RuntimeException("Tag not found"));
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public Optional<Tag> getTagByName(String name) {
        return tagRepository.findByName(name);
    }
   /* @Override
    public Task findTaskFromTag(Long taskId) {
        return taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found with id " + taskId));
    }*/
    @Override
    public List<Tag> findTagsByTaskId(Long taskId) {
        return taskRepository.findTagsByTaskId(taskId);
    }
}

