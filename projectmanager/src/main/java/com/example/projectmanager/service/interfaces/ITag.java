package com.example.projectmanager.service.interfaces;

import java.util.List;

import com.example.projectmanager.persistance.entities.Tag;

public interface ITag {
    Tag addTag(String name, Long taskId);

    Tag updateTag(Long id, String name, Long taskId);

    void deleteTag(Long id);

    Tag getTagById(Long id);

    List<Tag> getAllTags();

    List<Tag> findTagsByTaskId(Long taskId);
}
