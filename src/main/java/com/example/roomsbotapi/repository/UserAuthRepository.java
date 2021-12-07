package com.example.roomsbotapi.repository;

import com.example.roomsbotapi.models.Security.UsersAuth;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAuthRepository extends MongoRepository<UsersAuth, String> {

    Optional<UsersAuth> findByUsername(String username);

    Boolean existsByUsername(String username);
}
