package com.example.roomsbotapi;

import com.example.roomsbotapi.models.Security.ERole;
import com.example.roomsbotapi.models.Security.Role;
import com.example.roomsbotapi.models.Security.UsersAuth;
import com.example.roomsbotapi.repository.RoleRepository;
import com.example.roomsbotapi.repository.UserAuthRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class RoomsBotApiApplication {

    public static void main(String[] args) {
        System.out.println(LocalDate.now());
        SpringApplication.run(RoomsBotApiApplication.class, args);
    }

    @Bean
    CommandLineRunner init(UserAuthRepository repository, RoleRepository roleRepository) {
        return args -> {
            if (!repository.existsByUsername("rooms")) {

                Role role = new Role();
                role.setName(ERole.ROLE_ADMIN);
                roleRepository.save(role);

                UsersAuth user = new UsersAuth();
                user.setUsername("rooms");
                user.setPassword(new BCryptPasswordEncoder().encode("zsxadc1234"));


                user.setRoles(Collections.singleton(role));

                repository.save(user);
            }

            if (!repository.existsByUsername("client")) {
                Role role = new Role();
                role.setName(ERole.ROLE_CLIENT);
                roleRepository.save(role);

                UsersAuth user = new UsersAuth();
                user.setUsername("client");
                user.setPassword(new BCryptPasswordEncoder().encode("client228"));

                user.setRoles(Collections.singleton(role));

                repository.save(user);
            }

            if (!repository.existsByUsername("manager")) {
                Role role = new Role();
                role.setName(ERole.ROLE_MANAGER);
                roleRepository.save(role);

                UsersAuth user = new UsersAuth();
                user.setUsername("manager");
                user.setPassword(new BCryptPasswordEncoder().encode("manager228"));

                user.setRoles(Collections.singleton(role));

                repository.save(user);
            }

            if (!repository.existsByUsername("marketolog")) {
                Role role = new Role();
                role.setName(ERole.ROLE_MARKETOLOG);
                roleRepository.save(role);

                UsersAuth user = new UsersAuth();
                user.setUsername("marketolog");
                user.setPassword(new BCryptPasswordEncoder().encode("marketolog228"));

                user.setRoles(Collections.singleton(role));

                repository.save(user);
            }

        };
    }

}
