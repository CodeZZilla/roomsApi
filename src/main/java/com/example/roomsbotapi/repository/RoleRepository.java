package com.example.roomsbotapi.repository;

import com.example.roomsbotapi.models.Security.ERole;
import com.example.roomsbotapi.models.Security.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByName(ERole name);
}
