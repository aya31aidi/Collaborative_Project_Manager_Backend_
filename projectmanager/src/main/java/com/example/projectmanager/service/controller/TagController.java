package com.example.projectmanager.service.controller;

import com.example.projectmanager.persistance.entities.Tag;
import com.example.projectmanager.service.interfaces.ITag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private ITag tagService;

    // POST /tags?tag=urgent&taskId=3
    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestParam String tag, @RequestParam Long taskId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.addTag(tag, taskId));
    }

    // PUT /tags/5 {"name": "blocked"}, optionally ?taskId=7 to move it to another task
    @PutMapping("/{id}")
    public Tag updateTag(@PathVariable Long id, @RequestBody Tag tag,
                         @RequestParam(required = false) Long taskId) {
        return tagService.updateTag(id, tag.getName(), taskId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable Long id) {
        return tagService.getTagById(id);
    }

    @GetMapping
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    // An empty list rather than 204, so clients can always read a JSON array.
    @GetMapping("/task/{taskId}")
    public List<Tag> getTagsByTaskId(@PathVariable Long taskId) {
        return tagService.findTagsByTaskId(taskId);
    }
}
