package com.fitness.userservice.dto;

import com.fitness.userservice.model.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private  String id;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private UserRole role = UserRole.USER; ;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
