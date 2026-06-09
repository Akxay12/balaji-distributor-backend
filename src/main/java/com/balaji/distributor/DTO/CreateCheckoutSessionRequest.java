package com.balaji.distributor.DTO;

import lombok.Data;

@Data
public class CreateCheckoutSessionRequest {


    private Long basketId;

    private String paymentMethod;

    private String fullName;

    private String mobile;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String pincode;
}