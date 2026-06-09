package com.balaji.distributor.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Mobile number is required")
    private String mobileno;

    @NotBlank(message = "Password is required")
    private String password;
}
