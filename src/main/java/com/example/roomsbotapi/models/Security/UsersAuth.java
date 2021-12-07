package com.example.roomsbotapi.models.Security;

import com.example.roomsbotapi.models.Security.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Document
@NoArgsConstructor
public class UsersAuth {

    @Id
    private String id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 120)
    private String password;

    @DBRef
    private Set<Role> roles = new HashSet<>();


    public UsersAuth(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
