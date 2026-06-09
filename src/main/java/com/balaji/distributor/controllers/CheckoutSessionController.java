package com.balaji.distributor.controllers;

import com.balaji.distributor.DTO.CheckoutSessionResponse;
import com.balaji.distributor.DTO.CreateCheckoutSessionRequest;
import com.balaji.distributor.security.JwtService;
import com.balaji.distributor.service.CheckoutSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout-session")
@RequiredArgsConstructor

@CrossOrigin("*")
public class CheckoutSessionController {

    private final CheckoutSessionService
            sessionService;

    @Autowired
    private JwtService jwtService;


    // CREATE SESSION
    @PostMapping("/create")
    public CheckoutSessionResponse
    createSession(

            @RequestBody
            CreateCheckoutSessionRequest request,

            @RequestHeader("Authorization")
            String authHeader
    ) {

        String token =
                authHeader.substring(7);

        Long userId =
                jwtService.extractUserId(token);

        return sessionService
                .createSession(

                        userId,

                        request.getBasketId(),

                        request.getPaymentMethod(),

                        request.getFullName(),

                        request.getMobile(),

                        request.getAddressLine1(),

                        request.getAddressLine2(),

                        request.getCity(),

                        request.getState(),

                        request.getPincode()
                );
    }



    // GET SESSION

    @GetMapping("/{id}")
    public CheckoutSessionResponse
    getSession(

            @PathVariable Long id,

            @RequestHeader("Authorization")
            String authHeader
    ) {

        String token =
                authHeader.substring(7);

        Long userId =
                jwtService.extractUserId(token);

        return sessionService.getSession(
                userId,
                id
        );
    }



    // PAY NOW
    @PostMapping("/{id}/pay")
    public CheckoutSessionResponse
    payNow(

            @PathVariable Long id,

            @RequestHeader("Authorization")
            String authHeader
    ) {

        String token =
                authHeader.substring(7);

        Long userId =
                jwtService.extractUserId(token);

        return sessionService.payNow(
                userId,
                id
        );
    }


    @GetMapping("/public/{id}")
    public CheckoutSessionResponse
    getPublicSession(

            @PathVariable
            Long id
    ) {

        return sessionService
                .getPublicSession(id);
    }

    @PostMapping("/public/{id}/pay")
    public CheckoutSessionResponse
    payPublic(

            @PathVariable
            Long id
    ) {

        return sessionService
                .payPublic(id);
    }

}