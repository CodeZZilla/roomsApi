package com.example.roomsbotapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class News {

    @Id
    private String id;

    private String link;
    private String imageUrl;
    private String title;
    private String text;
}
