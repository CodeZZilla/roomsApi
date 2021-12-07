package com.example.roomsbotapi.payload.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}

