package com.balaji.distributor.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptResponse {

    // STORE INFO

    private String storeName;

    private String storeAddress;

    private String storePhone;

    private String storeEmail;

    private String storeGstNumber;


    // ORDER INFO

    private Long orderId;

    private String invoiceNumber;

    private LocalDateTime orderDate;

    private String paymentMethod;

    private String orderStatus;


    // CUSTOMER INFO

    private String customerName;

    private String shopName;

    private String customerMobile;

    private String customerGstNumber;


    // SHIPPING INFO

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String pincode;


    // ITEMS

    private List<ReceiptItemResponse> items;


    // SUMMARY

    private double subtotal;

    private double totalDiscount;

    private double taxableAmount;

    private double gstPercentage;

    private double totalGstAmount;

    private double shippingCharges;

    private double grandTotal;

    private int totalItems;

    private int totalQuantity;

    private String paymentStatus;


}