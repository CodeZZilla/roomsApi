package com.example.roomsbotapi.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@Data
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String id;
    private String username;
    private List<String> roles;

    public JwtResponse(String accessToken, String id, String username, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}
