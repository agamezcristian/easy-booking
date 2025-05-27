package com.easyschedule.app.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
