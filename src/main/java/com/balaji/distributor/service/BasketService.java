package com.balaji.distributor.service;

import com.balaji.distributor.exceptionHandler.ResourceNotFoundException;
import com.balaji.distributor.exceptionHandler.UnauthorizedException;
import org.springframework.transaction.annotation.Transactional;
import com.balaji.distributor.DTO.AddToBasketRequest;
import com.balaji.distributor.DTO.QuantityUpdateRequest;
import com.balaji.distributor.entity.*;
import com.balaji.distributor.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BasketService {

    @Autowired
    private BasketRepository basketRepo;

    @Autowired
    private BasketItemRepo basketItemRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ProductRepository productRepo;


    // CREATE BASKET
    @Transactional
    public Basket createBasket(Long userId) {

        User user = userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        // previous active -> saved

        List<Basket> userBaskets =
                basketRepo.findByUserId(userId);

        userBaskets.forEach(b -> {

            if ("ACTIVE".equals(b.getStatus())) {

                b.setStatus("SAVED");

                basketRepo.save(b);
            }
        });

        Basket basket = new Basket();

        basket.setUser(user);

        basket.setStatus("ACTIVE");

        basket.setCreatedAt(LocalDateTime.now());

        Basket latestBasket =
                basketRepo.findTopByUserIdOrderByBasketNumberDesc(
                        userId
                ).orElse(null);

        Integer nextBasketNumber =
                latestBasket != null
                        ? latestBasket.getBasketNumber() + 1
                        : 1;

        basket.setBasketNumber(nextBasketNumber);

        return basketRepo.save(basket);
    }


    // FETCH USER BASKETS

    public List<Basket> getUserBaskets(Long userId) {

        return basketRepo.findByUserId(userId);
    }


    // SWITCH ACTIVE BASKET
    @Transactional
    public Basket switchBasket(
            Long userId,
            Long basketId
    ) {

        List<Basket> baskets =
                basketRepo.findByUserId(userId);

        baskets.forEach(b -> {

            if ("ACTIVE".equals(b.getStatus())) {

                b.setStatus("SAVED");

                basketRepo.save(b);
            }
        });

        Basket basket = basketRepo.findByIdAndUserId(
                        basketId,
                        userId )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Basket not found"));

        basket.setStatus("ACTIVE");

        return basketRepo.save(basket);
    }



    // save basket
    public Basket saveBasket(

            Long userId,

            Long basketId
    ) {

        Basket basket =
                basketRepo.findById(basketId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Basket not found"
                                ));

        if (
                !basket.getUser()
                        .getId()
                        .equals(userId)
        ) {

            throw new UnauthorizedException(
                    "Unauthorized access"
            );
        }

        basket.setStatus("SAVED");

        return basketRepo.save(basket);
    }




    // DELETE BASKET
    @Transactional
    public void deleteBasket(

            Long userId,

            Long basketId
    ) {

        Basket basket = basketRepo.findById(basketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Basket not found"
                        ));

        if (
                !basket.getUser()
                        .getId()
                        .equals(userId)
        ) {

            throw new UnauthorizedException(
                    "Unauthorized access"
            );
        }

        basketItemRepo.deleteByBasketId(
                basketId
        );

        basketRepo.deleteById(
                basketId
        );
    }

    //======================================================

    //add to basket
    @Transactional
    public Basket addToBasket(

            Long userId,

            AddToBasketRequest request
    ) {

        // FIND ACTIVE BASKET
        Basket basket = basketRepo
                .findByUserIdAndStatus(
                        userId,
                        "ACTIVE"
                )
                .orElseGet(() ->
                        createBasket(
                                userId
                        )
                );

        // FIND PRODUCT
        Product product =
                productRepo.findById(
                        request.getProductId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found"
                        )
                );


        // CHECK EXISTING ITEM
        BasketItem basketItem =
                basketItemRepo
                        .findByBasketIdAndProductId(
                                basket.getId(),
                                product.getId()
                        )
                        .orElse(null);

        // UPDATE EXISTING
        if (basketItem != null) {

            basketItem.setQuantity(
                    basketItem.getQuantity()
                            + request.getQuantity()
            );

        }

        // CREATE NEW ITEM
        else {

            basketItem = new BasketItem();

            basketItem.setBasket(
                    basket
            );

            basketItem.setProduct(
                    product
            );

            basketItem.setQuantity(
                    request.getQuantity()
            );
        }

        basketItemRepo.save(
                basketItem
        );

        return basketRepo
                .findById(
                        basket.getId()
                )
                .orElseThrow(() ->
                new ResourceNotFoundException(
                        "Basket not found"
                )
        );
    }



    // Remove product from basket
    public void removeItemFromBasket(

            Long userId,

            Long basketId,

            Long productId
    ){

        Basket basket = basketRepo.findById(basketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Basket not found"
                        ));

        if (
                !basket.getUser()
                        .getId()
                        .equals(userId)
        ) {

            throw new UnauthorizedException(
                    "Unauthorized access"
            );
        }

        BasketItem basketItem =
                basketItemRepo
                        .findByBasketIdAndProductId(
                                basketId,
                                productId
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Basket item not found"
                                ));

        basketItemRepo.delete(
                basketItem
        );
    }


    //update quantity
    @Transactional
    public Basket updateQuantity(

            Long userId,

            QuantityUpdateRequest request
    ) {

        Basket basket = basketRepo.findById(
                        request.getBasketId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Basket not found"
                        ));

        if (
                !basket.getUser()
                        .getId()
                        .equals(userId)
        ) {

            throw new UnauthorizedException(
                    "Unauthorized access"
            );
        }

        BasketItem basketItem =
                basketItemRepo
                        .findByBasketIdAndProductId(
                                request.getBasketId(),
                                request.getProductId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Item not found"
                                )
                        );

        basketItem.setQuantity(
                request.getQuantity()
        );

        basketItemRepo.save(
                basketItem
        );

        return basket;
    }


    //CLEAR BASKET
    @Transactional
    public void clearBasket(

            Long userId,

            Long basketId
    ){

        Basket basket = basketRepo.findById(basketId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Basket not found"
                        ));

        if (
                !basket.getUser()
                        .getId()
                        .equals(userId)
        ) {

            throw new UnauthorizedException(
                    "Unauthorized access"
            );
        }

        basketItemRepo.deleteByBasketId(
                basket.getId()
        );
    }



}