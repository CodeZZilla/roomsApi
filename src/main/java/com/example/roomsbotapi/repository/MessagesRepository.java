package com.example.roomsbotapi.repository;

import com.example.roomsbotapi.models.Messages;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesRepository extends MongoRepository<Messages, String> {
}
