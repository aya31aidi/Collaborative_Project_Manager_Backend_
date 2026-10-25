package com.example.projectmanager.service.impliments;

import com.example.projectmanager.exception.NotFoundException;
import com.example.projectmanager.persistance.dao.TagRepository;
import com.example.projectmanager.persistance.dao.TaskRepository;
import com.example.projectmanager.persistance.entities.Tag;
import com.example.projectmanager.persistance.entities.Task;
import com.example.projectmanager.service.interfaces.ITag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TagImpl implements ITag {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Tag addTag(String name, Long taskId) {
        Task task = findTask(taskId);
        String clean = requireName(name);

        // Adding a label the task already has returns the existing one.
        for (Tag tag : task.getTags()) {
            if (tag.getName().equalsIgnoreCase(clean)) {
                return tag;
            }
        }
        Tag tag = new Tag(clean, task);
        task.getTags().add(tag);
        return tagRepository.save(tag);
    }

    @Override
    public Tag updateTag(Long id, String name, Long taskId) {
        Tag tag = getTagById(id);
        tag.setName(requireName(name));

        // Moving a tag to another task is optional.
        Task current = tag.getTask();
        if (taskId != null && (current == null || !taskId.equals(current.getId()))) {
            Task target = findTask(taskId);
            if (current != null) {
                current.getTags().remove(tag);
            }
            tag.setTask(target);
            target.getTags().add(tag);
        }
        return tag;
    }

    @Override
    public void deleteTag(Long id) {
        Tag tag = getTagById(id);
        if (tag.getTask() != null) {
            tag.getTask().getTags().remove(tag);
        }
        tagRepository.delete(tag);
    }

    @Override
    @Transactional(readOnly = true)
    public Tag getTagById(Long id) {
        return tagRepository.findById(id).orElseThrow(() -> new NotFoundException("Tag", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tag> findTagsByTaskId(Long taskId) {
        return tagRepository.findByTask_IdOrderByIdAsc(taskId);
    }

    private Task findTask(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Task", taskId));
    }

    private static String requireName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("A tag needs a name");
        }
        if (name.trim().length() > 40) {
            throw new IllegalArgumentException("A tag is limited to 40 characters");
        }
        return name.trim();
    }
}
