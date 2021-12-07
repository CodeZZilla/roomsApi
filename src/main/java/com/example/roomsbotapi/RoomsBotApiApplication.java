package com.example.roomsbotapi;

import com.example.roomsbotapi.models.Security.ERole;
import com.example.roomsbotapi.models.Security.Role;
import com.example.roomsbotapi.models.Security.UsersAuth;
import com.example.roomsbotapi.repository.RoleRepository;
import com.example.roomsbotapi.repository.UserAuthRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;
import java.util.Date;

@SpringBootApplication
@EnableScheduling
public class RoomsBotApiApplication {

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        System.out.println(new Date());
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

        };
    }

}
