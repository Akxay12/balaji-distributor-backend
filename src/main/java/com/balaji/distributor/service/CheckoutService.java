package com.balaji.distributor.service;

import com.balaji.distributor.DTO.CheckoutSummaryResponse;
import com.balaji.distributor.entity.Basket;
import com.balaji.distributor.entity.BasketItem;
import com.balaji.distributor.exceptionHandler.ResourceNotFoundException;
import com.balaji.distributor.exceptionHandler.UnauthorizedException;
import com.balaji.distributor.repository.BasketItemRepo;
import com.balaji.distributor.repository.BasketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.balaji.distributor.entity.StoreSettings;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final BasketRepository basketRepo;

    private final BasketItemRepo basketItemRepo;

    private final StoreSettingsService storeSettingsService;

    public CheckoutSummaryResponse getCheckoutSummary(

            Long userId,

            Long basketId
    ){

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

       List<BasketItem> basketItems =
        basketItemRepo
                .findByBasket_Id(
                        basket.getId()
                );

        // SUBTOTAL

        // SUBTOTAL

        double subtotal =

                basketItems.stream()

                        .mapToDouble(item -> {

                            double price =

                                    item.getProduct()
                                            .getPrice();

                            double discount =

                                    item.getProduct()
                                            .getDiscount();

                            double discountedPrice =

                                    price -
                                            (price * discount / 100);

                            return discountedPrice *
                                    item.getQuantity();
                        })

                        .sum();

        // STORE SETTINGS

        StoreSettings settings =
                storeSettingsService
                        .getSettings();

        // GST %

        double gstPercentage =
                settings.getGstPercentage();

        // GST AMOUNT

        double gstAmount =
                (subtotal * gstPercentage)
                        / 100;

        // SHIPPING

        double shippingCharges =
                settings.getShippingCharges();

        // FINAL TOTAL

        double finalTotal =
                subtotal
                        + gstAmount
                        + shippingCharges;

        return new CheckoutSummaryResponse(

                subtotal,

                gstPercentage,

                gstAmount,

                shippingCharges,

                finalTotal
        );
    }

}
