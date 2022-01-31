package com.example.roomsbotapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
public class TelegramApiService {

    private static final String TOKEN = "2069670508:AAFR_4gwUKymhGc7oiTLvq17d-nyYm6mY6A";
    private static final ObjectMapper mapper = new ObjectMapper();

    private final RestTemplate restTemplate;

    @Autowired
    public TelegramApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Async
    public CompletableFuture<byte[]> getUserPhoto(String idTelegram) throws JsonProcessingException {
        try {
            String filePath = Objects.requireNonNull(getFile(idTelegram)).path("result").path("file_path").toString().replace('"', ' ').trim();
            ResponseEntity<byte[]> imageString = restTemplate.getForEntity("https://api.telegram.org/file/bot" + TOKEN +
                    "/" + filePath, byte[].class);

            return CompletableFuture.completedFuture(imageString.getBody());
        } catch (Exception e) {
            return CompletableFuture.completedFuture(getNotFoundImage());
        }
    }

    private JsonNode getFile(String idTelegram) throws JsonProcessingException {
        JsonNode root = getUserProfilePhotoFileId(idTelegram);

        if (root.path("result").path("photos").toString().equals("[]")) {
            return null;
        }

        String fileId = root.path("result").path("photos").get(0).get(1).path("file_id").toString().replace('"', ' ').trim();
        ResponseEntity<String> response = restTemplate.getForEntity("https://api.telegram.org/bot" + TOKEN +
                "/getFile?file_id=" + fileId, String.class);

        return mapper.readTree(response.getBody());
    }

    private JsonNode getUserProfilePhotoFileId(String idTelegram) throws JsonProcessingException {

        ResponseEntity<String> response = restTemplate.getForEntity("https://api.telegram.org/bot" + TOKEN + "/getUserProfilePhotos?user_id=" +
                idTelegram, String.class);


        return mapper.readTree(response.getBody());
    }

    private byte[] getNotFoundImage() {
        ResponseEntity<byte[]> notFoundImage = restTemplate.getForEntity(
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRlf91yfOT2B7vCu4ikHj54dlXtsCAo7ZzeCw&usqp=CAU",
                byte[].class);

        return notFoundImage.getBody();
    }
}
