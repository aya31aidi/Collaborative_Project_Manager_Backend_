package com.example.projectmanager.service.interfaces;
import java.util.List;
import java.util.Optional;

import com.example.projectmanager.persistance.entities.Tag;
public interface ITag {
    void addTag(Tag tag);
    void updateTag(Long id, Tag tag);
    void deleteTag(Long id);
    Tag getTagById(Long id);
    List<Tag> getAllTags();
    Optional<Tag> getTagByName(String name);
}
