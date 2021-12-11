package com.example.roomsbotapi.controllers;

import com.example.roomsbotapi.models.News;
import com.example.roomsbotapi.repository.NewsRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
@CrossOrigin
@AllArgsConstructor
public class NewsController {

    private final NewsRepository newsRepository;

    @GetMapping
    public List<News> getAll () {
        return newsRepository.findAll();
    }

    @GetMapping("/{id}")
    public News getOne(@PathVariable String id) {
        return newsRepository.findById(id).get();
    }

    @PostMapping("/add")
    public ResponseEntity<News> addNews(@RequestBody News news) {
        return new ResponseEntity<>(newsRepository.save(news), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<News> update(@PathVariable String id, @RequestBody News news) {
        News newsFromDb = newsRepository.findById(id).orElse(null);

        if (newsFromDb == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        newsFromDb.setLink(news.getLink());
        newsFromDb.setImageUrl(news.getImageUrl());
        newsFromDb.setText(news.getText());
        newsFromDb.setTitle(news.getTitle());

        newsRepository.save(newsFromDb);

        return ResponseEntity.ok(newsFromDb);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<List<News>> delete(@PathVariable String id) {
        newsRepository.deleteById(id);
        return ResponseEntity.ok(newsRepository.findAll());
    }
}
