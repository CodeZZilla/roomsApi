package com.example.roomsbotapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@SpringBootTest
class RoomsBotApiApplicationTests {

    @Autowired
    RestTemplate restTemplate;

    @Test
    void contextLoads() throws JsonProcessingException {
        ResponseEntity<String> notFoundImage = restTemplate.getForEntity(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRlf91yfOT2B7vCu4ikHj54dlXtsCAo7ZzeCw&usqp=CAU",
                String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(notFoundImage.getBody());
        System.out.println(root);

    }

}
