package com.fitness.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "email is required")
    @Email(message = "email is not valid")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 8,message = "Password must have atleast 8 charcters")
    private String password;
    private String firstname;
    private String lastname;
    
}
