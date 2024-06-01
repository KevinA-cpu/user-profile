package com.udpt.userprofile.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

@Data
public class RegisterDto {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String address;

    @Past(message = "Date of birth should be in the past")
    @NotNull(message = "Date of birth is required")
    private Date dateOfBirth;


    @NotBlank(message = "Email is required")
    @Email(message="Invalid email")
    private String email;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message="Password is required")
    private String password;
}
