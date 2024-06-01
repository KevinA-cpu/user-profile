package com.udpt.userprofile.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoginDto {
    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message="Password is required")
    private String password;
}
