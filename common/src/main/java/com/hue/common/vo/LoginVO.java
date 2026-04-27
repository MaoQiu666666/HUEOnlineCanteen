package com.hue.common.vo;

import lombok.Data;

import java.util.List;

@Data
public class LoginVO {
    private String token;
    private Long userId;
    private String username;
    private String role;
    public LoginVO(String token, Long userId, String username, String role) {
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.role = role;
    }
}
