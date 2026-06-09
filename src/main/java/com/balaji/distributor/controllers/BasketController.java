package com.balaji.distributor.controllers;


import com.balaji.distributor.DTO.AddToBasketRequest;
import com.balaji.distributor.DTO.QuantityUpdateRequest;
import com.balaji.distributor.entity.Basket;
import com.balaji.distributor.security.JwtService;
import com.balaji.distributor.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/basket")
@CrossOrigin("*")
public class BasketController {

    @Autowired
    private BasketService basketService;

    @Autowired
    private JwtService jwtService;


    // CREATE BASKET
    @PostMapping("/create")
    public ResponseEntity<Basket> createBasket(
            @RequestHeader("Authorization")
            String authHeader
    ) {

        String token =
                authHeader.substring(7);

        Long userId =
                jwtService.extractUserId(token);

        return ResponseEntity.ok(
                basketService.createBasket(userId)
        );
    }


    // FETCH USER BASKETS
    @GetMapping("/fetch")
    public ResponseEntity<List<Basket>> getUserBaskets(
            @RequestHeader("Authorization")
            String authHeader
    ) {

        String token =
                authHeader.substring(7);

        Long userId =
                jwtService.extractUserId(token);

        return ResponseEntity.ok(
                basketService.getUserBaskets(userId)
        );
    }


    // SWITCH BASKET
    @PutMapping("/switch/{basketId}")
    public ResponseEntity<Basket> switchBasket(
            @PathVariable Long basketId,
            @RequestHeader("Authorization")
            String authHeader
    ) {

        String token =
                authHeader.substring(7);

        Long userId =
                jwtService.extractUserId(token);

        return ResponseEntity.ok(
                basketService.switchBasket(
                        userId,
                        basketId
                )
        );
    }


    // SAVE BASKET
    @PutMapping("/save/{basketId}")
    public ResponseEntity<Basket> saveBasket(

            @PathVariable Long basketId,

            @RequestHeader("Authorization")
            String authHeader
    ) {

        String token =
                authHeader.substring(7);

        Long userId =
                jwtService.extractUserId(token);

        return ResponseEntity.ok(
                basketService.saveBasket(
                        userId,
                        basketId
                )
        );
    }





    // DELETE BASKET
    @DeleteMapping("/delete/{basketId}")
    public ResponseEntity<String> deleteBasket(

            @PathVariable Long basketId,

            @RequestHeader("Authorization")
            String authHeader
    ) {

        String token =
                authHeader.substring(7);

        Long userId =
                jwtService.extractUserId(token);

        basketService.deleteBasket(
                userId,
                basketId
        );

        return ResponseEntity.ok(
                "Basket deleted successfully"
        );
    }

    // ====================================================

    //BasketItems  endpoints


    // add product in basket
    @PostMapping("/add")
    public ResponseEntity<Basket> addToBasket(

            @RequestBody
            AddToBasketRequest request,

            @RequestHeader("Authorization")
            String authHeader
    ) {

        String token =
                authHeader.substring(7);

        Long userId =
                jwtService.extractUserId(token);

        return ResponseEntity.ok(
                basketService.addToBasket(
                        userId,
                        request
                )
        );
    }

    //remove single item
    // REMOVE PRODUCT FROM BASKET

    @DeleteMapping("/item/{basketId}/{productId}")
    public ResponseEntity<String>
    removeItemFromBasket(

            @PathVariable Long basketId,

            @PathVariable Long productId,

            @RequestHeader("Authorization")
            String authHeader
    ) {

        String token =
                authHeader.substring(7);

        Long userId =
                jwtService.extractUserId(token);

        basketService.removeItemFromBasket(
                userId,
                basketId,
                productId
        );

        return ResponseEntity.ok(
                "Product removed successfully"
        );
    }


    //update quantity
    @PutMapping("/item/quantity")
    public ResponseEntity<Basket>
    updateQuantity(

            @RequestBody
            QuantityUpdateRequest request,

            @RequestHeader("Authorization")
            String authHeader
    ) {

        String token =
                authHeader.substring(7);

        Long userId =
                jwtService.extractUserId(token);

        return ResponseEntity.ok(
                basketService.updateQuantity(
                        userId,
                        request
                )
        );
    }



    // CLEAR BASKET
    @DeleteMapping("/clear/{basketId}")
    public ResponseEntity<String> clearBasket(

            @PathVariable Long basketId,

            @RequestHeader("Authorization")
            String authHeader
    ) {

        String token =
                authHeader.substring(7);

        Long userId =
                jwtService.extractUserId(token);

        basketService.clearBasket(
                userId,
                basketId
        );

        return ResponseEntity.ok(
                "Basket cleared successfully"
        );
    }

}
