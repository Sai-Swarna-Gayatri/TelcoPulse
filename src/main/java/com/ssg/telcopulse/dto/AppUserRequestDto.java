package com.ssg.telcopulse.dto;

import com.ssg.telcopulse.model.UserRole;

import jakarta.validation.constraints.*;

public record AppUserRequestDto(
    @NotBlank(message="User name is mandatory.")
    @Size(max=10,message="User Name can not exceed 10 characters.")
    @Pattern(
    regexp = "^[a-zA-Z0-9]+$",
    message = "User name can contain only letters and numbers with atleast one letter")
    String userName,

    @NotBlank(message="Password can not be blank.")
    @Pattern(
    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
    message = """
        Password must be at least 8 characters long and contain:
        - At least one lowercase letter.
        - At least one uppercase letter.
        - At least one digit.
        - At least one special character from @#$%^&+=!
        """)
    String password,
    
    UserRole role) {

}
