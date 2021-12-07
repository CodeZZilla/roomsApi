package com.example.roomsbotapi.repository;

import com.example.roomsbotapi.models.Groups;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupsRepository extends MongoRepository<Groups, String> {
}
