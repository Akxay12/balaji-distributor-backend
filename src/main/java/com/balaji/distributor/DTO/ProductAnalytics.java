package com.balaji.distributor.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAnalytics {

    private double totalQuantitySold;

    private double totalRevenue;

    private int ordersCount;

    private String topProduct;
}
