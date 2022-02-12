package com.example.roomsbotapi.controllers;

import com.example.roomsbotapi.models.Messages;
import com.example.roomsbotapi.repository.MessagesRepository;
import com.example.roomsbotapi.services.TelegramApiService;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@RestController
@CrossOrigin
@RequestMapping("/api/message")
@NoArgsConstructor
@Slf4j
public class MessagesController {

    private MessagesRepository messagesRepository;
    private TelegramApiService telegramApiService;

    @Autowired
    public MessagesController(MessagesRepository messagesRepository,  TelegramApiService telegramApiService) {
        this.messagesRepository = messagesRepository;
        this.telegramApiService = telegramApiService;
    }

    @PostMapping("/add")
    public ResponseEntity<Messages> addMessage(@RequestBody Messages messages) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    for (var id : messages.getTelegramIds()) {
                        telegramApiService.sendMessage(id, messages.getMessageText());
                    }
                    log.info("send " + LocalDateTime.now());
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }
            }
        };
        Messages messagesSaved = messagesRepository.save(messages);
        new Timer("Timer message").schedule(task, new Date(Long.parseLong(messages.getTime())));

        return new ResponseEntity<>(messagesSaved, HttpStatus.CREATED);
    }
}
