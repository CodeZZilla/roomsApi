package com.example.roomsbotapi.repository;

import com.example.roomsbotapi.models.MessageSupportUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageSupportUserRepository extends MongoRepository<MessageSupportUser, String> {

}
