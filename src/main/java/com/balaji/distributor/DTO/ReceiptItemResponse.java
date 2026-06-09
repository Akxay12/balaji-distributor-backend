package com.balaji.distributor.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptItemResponse {

    private String productName;

    private String unit;

    private int quantity;

    private double mrp;

    private double discountPercent;

    private double discountedPrice;

    private double taxableAmount;

    private double gstPercent;

    private double gstAmount;

    private double finalTotal;

}
