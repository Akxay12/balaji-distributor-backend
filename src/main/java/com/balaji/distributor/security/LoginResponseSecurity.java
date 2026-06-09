package com.balaji.distributor.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponseSecurity {

    private String token;

    private Long userId;

    private String username;

    private String role;

    private String mobileno;

    private String email;

    private String shopname;

    private String gstnumber;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private Long pincode;
}
