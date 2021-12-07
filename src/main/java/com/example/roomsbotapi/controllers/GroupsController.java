package com.example.roomsbotapi.controllers;

import com.example.roomsbotapi.models.Groups;
import com.example.roomsbotapi.services.GroupsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/group")
@AllArgsConstructor
public class GroupsController {

    private final GroupsService groupsService;

    @GetMapping
    public List<Groups> getAllGroups() {
        return groupsService.findAll();
    }

    @GetMapping("/{id}")
    public Groups getGroup(@PathVariable String id) {
        return groupsService.findById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<Groups> addGroup(@RequestBody Groups groups) {
        return new ResponseEntity<>(groupsService.save(groups), HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Groups> updateGroup(@PathVariable String id, @RequestBody Groups groups) {
        Groups groupFromDb = groupsService.findById(id);

        if (groupFromDb == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        groupFromDb.setNameGroup(groups.getNameGroup());
        groupFromDb.setUsers(groups.getUsers());

        return ResponseEntity.ok(groupsService.save(groupFromDb));
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable String id) {
        groupsService.deleteById(id);
    }

}
