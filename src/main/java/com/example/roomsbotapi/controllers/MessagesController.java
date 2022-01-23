package com.example.roomsbotapi.controllers;

import com.example.roomsbotapi.models.Messages;
import com.example.roomsbotapi.repository.MessagesRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
    private RestTemplate restTemplate;

    @Autowired
    public MessagesController(MessagesRepository messagesRepository, RestTemplate restTemplate) {
        this.messagesRepository = messagesRepository;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/add")
    public ResponseEntity<Messages> addMessage(@RequestBody Messages messages) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    for (var id : messages.getTelegramIds()) {
                        restTemplate.getForEntity(
                                "https://api.telegram.org/bot2069670508:AAFR_4gwUKymhGc7oiTLvq17d-nyYm6mY6A/sendMessage?chat_id=" + id
                                        + "&text=" + messages.getMessageText()
                                , String.class);
                        System.out.println("send");
                    }
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                }
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, new Date(messages.getTime()));

        return new ResponseEntity<>(messagesRepository.save(messages), HttpStatus.CREATED);
    }
}
