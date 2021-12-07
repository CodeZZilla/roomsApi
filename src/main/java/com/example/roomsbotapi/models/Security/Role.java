package com.example.roomsbotapi.models.Security;

import com.example.roomsbotapi.models.Security.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Role {

    @Id
    private String id;

    private ERole name;
}
