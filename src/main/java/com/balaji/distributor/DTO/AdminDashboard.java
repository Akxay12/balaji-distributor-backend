package com.balaji.distributor.DTO;

import com.balaji.distributor.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboard {

    private long totalProducts;

    private long totalOrders;

    private double totalRevenue;

    private long totalPartners;

    private List<Order> recentOrders;
}
