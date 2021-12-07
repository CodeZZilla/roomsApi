package com.example.roomsbotapi.controllers;

import com.example.roomsbotapi.models.Messages;
import com.example.roomsbotapi.services.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/message")
@CrossOrigin
public class MessagesController {

    private final MessageService messageService;

    @PostMapping("/add")
    public ResponseEntity<Messages> addMessage(@RequestBody Messages messages) {
        messageService.deleteAll();
        return new ResponseEntity<>(messageService.save(messages), HttpStatus.CREATED);
    }

    @GetMapping("/find")
    @ResponseBody
    public ResponseEntity<Messages> find() {
        List<Messages> messagesList = messageService.findAll();
        System.out.println(messagesList);

        if (messagesList.size() == 0)
            return ResponseEntity.ok(new Messages());

        return ResponseEntity.ok(messagesList.get(messagesList.size() - 1));
    }
}
