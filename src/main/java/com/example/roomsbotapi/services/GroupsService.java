package com.example.roomsbotapi.services;

import com.example.roomsbotapi.models.Groups;
import com.example.roomsbotapi.repository.GroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupsService {

    private final GroupsRepository repository;

    @Autowired
    public GroupsService(GroupsRepository repository) {
        this.repository = repository;
    }

    public List<Groups> findAll() {
        return repository.findAll();
    }

    public Groups findById(String id) {
        return repository.findById(id).orElse(new Groups());
    }

    public void deleteById(String id) {
        repository.deleteById(id);
    }

    public Groups save(Groups groups) {
       return repository.save(groups);
    }

    public void saveAll(List<Groups> groups) {
        repository.saveAll(groups);
    }
}
