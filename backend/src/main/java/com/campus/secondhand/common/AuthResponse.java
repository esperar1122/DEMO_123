package com.campus.secondhand.common;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private Long userId;
    private String username;
    private Integer creditScore;
    private Integer role;

    public AuthResponse(String token, Long userId, String username, Integer creditScore, Integer role) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.creditScore = creditScore;
        this.role = role;
    }
}