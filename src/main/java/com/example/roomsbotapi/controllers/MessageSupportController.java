package com.example.roomsbotapi.controllers;

import com.example.roomsbotapi.models.MessageSupportUser;
import com.example.roomsbotapi.models.User;
import com.example.roomsbotapi.services.MessageSupportUserService;
import com.example.roomsbotapi.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@AllArgsConstructor
@RequestMapping("/api/messageSupport")
public class MessageSupportController {

    private MessageSupportUserService messageSupportUserService;
    private UserService userService;

    @GetMapping
    public List<MessageSupportUser> getAllMessagesSupport() {
        return messageSupportUserService.findAll();
    }

    @PostMapping("/add/{idTelegram}")
    public ResponseEntity<MessageSupportUser> addMessageUser(@PathVariable String idTelegram, @RequestBody MessageSupportUser messageSupportUser) {
        User user = userService.findByIdTelegram(idTelegram);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        messageSupportUser.setUser(user);
        return ResponseEntity.ok(messageSupportUserService.save(messageSupportUser));
    }
}
