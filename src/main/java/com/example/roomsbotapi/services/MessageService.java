package com.example.roomsbotapi.services;

import com.example.roomsbotapi.models.Messages;
import com.example.roomsbotapi.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository repository;

    @Autowired
    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    public Messages save(Messages messages) {
        return repository.save(messages);
    }

    public Messages findById(String id) {
        return repository.findById(id).get();
    }

    public List<Messages> findAll() {
        return repository.findAll();
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
