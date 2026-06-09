package com.balaji.distributor.DTO;

import lombok.Data;



@Data
public class UserUpdateRequest {

    private String username;

    private String email;

    private String shopname;

    private String gstnumber;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private Long pincode;

    private String password;
}
