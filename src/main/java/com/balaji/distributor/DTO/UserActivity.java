package com.balaji.distributor.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserActivity {

    private Long userId;

    private long totalOrders;

    private double lifetimePurchase;
}
