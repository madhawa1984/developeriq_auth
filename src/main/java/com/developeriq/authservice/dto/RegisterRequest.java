package com.developeriq.authservice.dto;


import com.developeriq.authservice.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private Role role;
}
