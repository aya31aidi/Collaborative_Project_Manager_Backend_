package com.example.projectmanager.service.controller;

import com.example.projectmanager.persistance.entities.Tag;
import com.example.projectmanager.service.interfaces.ITag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private ITag tagService;

    @PostMapping
    public void createTag(@RequestBody Tag tag) {
        tagService.addTag(tag);
    }

    @PutMapping("/{id}")
    public void updateTag(@PathVariable Long id, @RequestBody Tag tag) {
        tagService.updateTag(id, tag);
    }

    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
    }

    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable Long id) {
        return tagService.getTagById(id);
    }

    @GetMapping("")
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/search")
    public Tag getTagByName(@RequestParam String name) {
        return tagService.getTagByName(name);
    }
}
