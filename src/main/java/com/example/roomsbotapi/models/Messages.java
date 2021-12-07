package com.example.roomsbotapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Messages {

    @Id
    private String id;

    private List<String> userTelegramId;

    private String messageText;
}
