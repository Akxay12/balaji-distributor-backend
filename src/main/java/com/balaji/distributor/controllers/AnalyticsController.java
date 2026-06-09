package com.balaji.distributor.controllers;

import com.balaji.distributor.DTO.ProductAnalytics;
import com.balaji.distributor.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analytics")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService
            analyticsService;




    // ALL PRODUCTS
    @GetMapping("/products")
    public ProductAnalytics getAllAnalytics() {

        return analyticsService
                .getAnalytics(null);
    }



    // SINGLE PRODUCT
    @GetMapping("/products/{productId}")

    public ProductAnalytics getProductAnalytics( @PathVariable Long productId ) {

        return analyticsService
                .getAnalytics(productId);
    }
}
