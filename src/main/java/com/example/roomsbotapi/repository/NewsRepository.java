package com.example.roomsbotapi.repository;

import com.example.roomsbotapi.models.News;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends MongoRepository<News, String> {
}
