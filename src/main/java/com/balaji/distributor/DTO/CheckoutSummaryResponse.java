package com.balaji.distributor.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckoutSummaryResponse {

    private Double subtotal;

    private Double gstPercentage;

    private Double gstAmount;

    private Double shippingCharges;

    private Double finalTotal;

}
