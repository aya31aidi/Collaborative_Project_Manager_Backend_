package com.example.projectmanager.service.impliments;

import com.example.projectmanager.persistance.dao.TagRepository;
import com.example.projectmanager.persistance.entities.Tag;
import com.example.projectmanager.service.interfaces.ITag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagImpl implements ITag {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public void addTag(Tag tag) {
        tagRepository.save(tag);
    }

    @Override
    public void updateTag(Long id, Tag tag) {
        Tag existingTag = tagRepository.findById(id).orElseThrow(() -> new RuntimeException("Tag not found"));
        existingTag.setName(tag.getName());
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
}

