package com.balaji.distributor.controllers;

import com.balaji.distributor.DTO.CheckoutSummaryResponse;
import com.balaji.distributor.security.JwtService;
import com.balaji.distributor.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @Autowired
    private JwtService jwtService;



    // GET CHECKOUT SUMMARY
    @GetMapping("/summary/{basketId}")
    public ResponseEntity<CheckoutSummaryResponse>
    getCheckoutSummary(

            @PathVariable Long basketId,

            @RequestHeader("Authorization")
            String authHeader
    ) {

        String token =
                authHeader.substring(7);

        Long userId =
                jwtService.extractUserId(token);

        return ResponseEntity.ok(

                checkoutService.getCheckoutSummary(

                        userId,

                        basketId
                )
        );
    }

}
