package com.example.projectmanager.service.interfaces;
import java.util.List;
import java.util.Optional;

import com.example.projectmanager.persistance.entities.Tag;
import com.example.projectmanager.persistance.entities.Task;
public interface ITag {
    void deleteTag(Long id);
    Tag getTagById(Long id);
    List<Tag> getAllTags();
    Optional<Tag> getTagByName(String name);
	void addTag(String name, Long taskId);
	void updateTag(Long id, Tag tag, Long idtask);
	Task findTaskFromTag(Long taskId);
}
