package com.balaji.distributor.service;

import com.balaji.distributor.DTO.AdminDashboard;
import com.balaji.distributor.entity.Order;
import com.balaji.distributor.repository.OrderRepository;
import com.balaji.distributor.repository.ProductRepository;
import com.balaji.distributor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final ProductRepository
            productRepo;

    private final OrderRepository
            orderRepo;

    private final UserRepository
            userRepo;

    public AdminDashboard
    getDashboardData() {

        // TOTAL PRODUCTS

        long totalProducts =
                productRepo.count();

        // TOTAL ORDERS

        long totalOrders =
                orderRepo.count();

        // TOTAL USERS / PARTNERS

        long totalPartners =
                userRepo.count();

        // ALL ORDERS

        List<Order> orders =
                orderRepo.findAll();

        // TOTAL REVENUE

        double totalRevenue = 0;

        for (Order order : orders) {

            totalRevenue +=
                    order.getTotalAmount();
        }

        // ROUND REVENUE

        totalRevenue =
                Math.round(
                        totalRevenue * 100.0
                ) / 100.0;

        // RECENT ORDERS

        orders.sort(

                Comparator.comparing(
                        Order::getCreatedAt
                ).reversed()
        );

        List<Order> recentOrders =
                orders.stream()
                        .limit(5)
                        .toList();

        return new AdminDashboard(

                totalProducts,

                totalOrders,

                totalRevenue,

                totalPartners,

                recentOrders
        );
    }
}
