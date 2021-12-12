package com.example.roomsbotapi.services;

import com.example.roomsbotapi.models.MessageSupportUser;
import com.example.roomsbotapi.repository.MessageSupportUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageSupportUserService {

    private final MessageSupportUserRepository repository;

    public MessageSupportUserService(MessageSupportUserRepository repository) {
        this.repository = repository;
    }

    public List<MessageSupportUser> findAll() {
        return repository.findAll();
    }

    public MessageSupportUser save(MessageSupportUser messageSupportUser) {
        return repository.save(messageSupportUser);
    }
}
