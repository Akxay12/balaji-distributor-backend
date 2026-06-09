package com.balaji.distributor.controllers;

import com.balaji.distributor.entity.Order;
import com.balaji.distributor.security.JwtService;
import com.balaji.distributor.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.balaji.distributor.DTO.ReceiptResponse;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin("*")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService
            orderService;

    @Autowired
    private JwtService jwtService;



    // CREATE ORDER
    @PostMapping("/create")
    public Order createOrder(

            @RequestHeader("Authorization")
            String authHeader,

            @RequestParam Long basketId,

            @RequestParam String paymentMethod,

            @RequestParam String fullName,

            @RequestParam String mobile,

            @RequestParam String addressLine1,

            @RequestParam String addressLine2,

            @RequestParam String city,

            @RequestParam String state,

            @RequestParam String pincode
    ) {

        String token =
                authHeader.substring(7);

        Long userId =
                jwtService.extractUserId(token);

        return orderService.createOrder(
                userId,
                basketId,
                paymentMethod,
                fullName,
                mobile,
                addressLine1,
                addressLine2,
                city,
                state,
                pincode
        );
    }



    // ADMIN ORDERS

    @GetMapping("/all")

    public List<Order>
    getAllOrders() {

        return orderService
                .getAllOrders();
    }

    // USER HISTORY

    @GetMapping("/user")
    public List<Order> getUserOrders(

            @RequestHeader("Authorization")
            String authHeader
    ) {

        String token =
                authHeader.substring(7);

        Long userId =
                jwtService.extractUserId(token);

        return orderService.getUserOrders(userId);
    }


    // UPDATE STATUS

    @PutMapping("/{orderId}/status")

    public Order updateStatus(

            @PathVariable Long orderId,

            @RequestParam String status

    ) {

        return orderService
                .updateStatus(
                        orderId,
                        status
                );
    }



// =============================================================
//                  Receipt endpoints



    @GetMapping("/receipt/{id}")
    public ReceiptResponse getReceipt(

            @PathVariable Long id,

            @RequestHeader("Authorization")
            String authHeader
    ) {

        String token =
                authHeader.substring(7);

        Long userId =
                jwtService.extractUserId(token);

        return orderService.getReceiptByOrderId(
                userId,
                id
        );
    }


}