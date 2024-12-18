package com.example.projectmanager.service.controller;

import com.example.projectmanager.persistance.entities.Tag;
import com.example.projectmanager.persistance.entities.Task;
import com.example.projectmanager.service.interfaces.ITag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private ITag tagService;

    @PostMapping
    public void createTag(@RequestParam String tag, @RequestParam Long taskId) {
        tagService.addTag(tag, taskId);
        System.out.println("Reçu : " + tag + " pour la tâche ID : " + taskId);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateTag(
            @PathVariable Long id,         
            @RequestBody Tag tag,        
            @RequestParam Long taskId) {  
    	Map<String, String> response = new HashMap<>();
            tagService.updateTag(id, tag, taskId); 
            return ResponseEntity.ok(response);
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

    /*@GetMapping("/search")
    public Tag getTagByName(@RequestParam String name) {
        return tagService.getTagByName(name);
    }*/
   /* @GetMapping("/task/{taskId}")
    public ResponseEntity<Task> getTaskByTag(@PathVariable Long taskId) {
        try {
            Task task = tagService.findTaskFromTag(taskId);
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }*/
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<Tag>> getTagsByTaskId(@PathVariable Long taskId) {
        List<Tag> tags = tagService.findTagsByTaskId(taskId);
        if (tags.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tags); 
    }
    
}
