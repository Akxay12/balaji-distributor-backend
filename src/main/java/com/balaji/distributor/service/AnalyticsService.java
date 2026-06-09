package com.balaji.distributor.service;

import com.balaji.distributor.DTO.ProductAnalytics;
import com.balaji.distributor.entity.Order;
import com.balaji.distributor.entity.OrderItem;
import com.balaji.distributor.entity.Product;
import com.balaji.distributor.repository.OrderRepository;
import com.balaji.distributor.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.balaji.distributor.exceptionHandler.ResourceNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final OrderRepository orderRepo;

    private final ProductRepository productRepo;

    public ProductAnalytics getAnalytics(Long productId) {

        if (productId != null) {

            productRepo.findById(productId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Product not found"
                            ));
        }

        List<Order> orders =
                orderRepo.findAll();

        double totalQuantitySold = 0;

        double totalRevenue = 0;

        int ordersCount = 0;

        Map<Long, Double> productSales =
                new HashMap<>();

        for (Order order : orders) {

            boolean orderContainsProduct =
                    false;

            for (OrderItem item :
                    order.getItems()) {

                // TOP PRODUCT

                productSales.put(

                        item.getProductId(),

                        productSales.getOrDefault(
                                item.getProductId(),
                                0.0
                        ) + item.getQuantity()
                );

                // ALL PRODUCTS

                if (productId == null) {

                    totalQuantitySold +=
                            item.getQuantity();

                    orderContainsProduct =
                            true;

                }

                // SINGLE PRODUCT

                else if (

                        item.getProductId()
                                .equals(productId)

                ) {

                    totalQuantitySold +=
                            item.getQuantity();

                    totalRevenue +=
                            item.getFinalPrice();

                    orderContainsProduct =
                            true;
                }
            }

            if (productId == null) {

                totalRevenue +=
                        order.getTotalAmount();
            }


            if (orderContainsProduct) {

                ordersCount++;
            }
        }

        // FIND TOP PRODUCT

        Long topProductId = null;

        double maxSold = 0;

        for (Map.Entry<Long, Double> entry :
                productSales.entrySet()) {

            if (entry.getValue() > maxSold) {

                maxSold =
                        entry.getValue();

                topProductId =
                        entry.getKey();
            }
        }

        String topProductName = "N/A";

        if (topProductId != null) {

            Product topProduct =
                    productRepo.findById(
                            topProductId
                    ).orElse(null);

            if (topProduct != null) {

                topProductName =
                        topProduct.getName();
            }
        }

        return new ProductAnalytics(

                totalQuantitySold,

                totalRevenue,

                ordersCount,

                topProductName
        );
    }
}