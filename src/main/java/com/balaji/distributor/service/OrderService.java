package com.balaji.distributor.service;


import com.balaji.distributor.DTO.ReceiptItemResponse;
import com.balaji.distributor.DTO.ReceiptResponse;
import com.balaji.distributor.exceptionHandler.BadRequestException;
import com.balaji.distributor.exceptionHandler.ResourceNotFoundException;
import java.util.ArrayList;
import com.balaji.distributor.entity.*;
import com.balaji.distributor.exceptionHandler.UnauthorizedException;
import com.balaji.distributor.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.transaction.Transactional;




@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepo;

    private final BasketRepository basketRepo;

    private final BasketItemRepo basketItemRepo;

    private final ProductRepository productRepo;

    private final UserRepository userRepo;

    private final StoreSettingsService
            storeSettingsService;


    @Transactional
    public Order createOrder(

            Long userId,

            Long basketId,

            String paymentMethod,

            String fullName,

            String mobile,

            String addressLine1,

            String addressLine2,

            String city,

            String state,

            String pincode

    ) {



        // USER

        User user =
                userRepo.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                ));

        // BASKET

        Basket basket =
                basketRepo.findById(basketId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Basket not found"
                                ));

        // ITEMS

        List<BasketItem> basketItems =
                basketItemRepo
                        .findByBasketId(
                                basketId
                        );

        if (!basket.getUser().getId().equals(userId)) {

            throw new UnauthorizedException(
                    "Basket does not belong to user"
            );
        }

        if (basketItems.isEmpty()) {

            throw new BadRequestException(
                    "Basket is empty"
            );
        }

        // SETTINGS

        StoreSettings settings =
                storeSettingsService
                        .getSettings();

        // ORDER

        Order order =
                new Order();

        order.setItems(
                new ArrayList<>()
        );

        order.setUser(user);


        // check valid payment method is not dismiss
        if (paymentMethod == null || (!paymentMethod.equals("COD") && !paymentMethod.equals("UPI"))
        ) {
            throw new BadRequestException(
                    "Invalid payment method"
            );
        }

        order.setPaymentMethod(
                paymentMethod
        );


        order.setStatus(
                "Order Placed"
        );

        order.setCreatedAt(
                LocalDateTime.now()
        );

        // SHIPPING SNAPSHOT

        // check if order address is not given by user
        if (
                fullName == null
                        || fullName.isBlank()
                        || mobile == null
                        || mobile.isBlank()
                        || addressLine1 == null
                        || addressLine1.isBlank()
                        || city == null
                        || city.isBlank()
                        || state == null
                        || state.isBlank()
                        || pincode == null
                        || pincode.isBlank()
        ) {

            throw new BadRequestException(
                    "Shipping details are required"
            );
        }

        order.setFullName(fullName);

        order.setMobile(mobile);

        order.setAddressLine1(
                addressLine1
        );

        order.setAddressLine2(
                addressLine2
        );

        order.setCity(city);


        order.setState(state);

        order.setPincode(pincode);

        // TOTALS

        double subtotal = 0;

        for (BasketItem item :
                basketItems) {

            Product product =
                    item.getProduct();

            if (product == null) {

                throw new ResourceNotFoundException(
                        "Product not found in basket"
                );
            }

            // STOCK VALIDATION

            if (
                    product.getStock()
                            < item.getQuantity()
            ) {

                throw new BadRequestException(
                        "Not enough stock for "
                                + product.getName()
                );
            }

            // DISCOUNTED PRICE

            double discountedPrice =

                    product.getPrice()

                            -

                            (
                                    product.getPrice()
                                            *
                                            product.getDiscount()
                                            / 100
                            );

            double finalPrice =

                    discountedPrice
                            *
                            item.getQuantity();

            subtotal += finalPrice;

            // ORDER ITEM

            OrderItem orderItem =
                    new OrderItem();

            orderItem.setOrder(order);

            orderItem.setProductId(
                    product.getId()
            );

            orderItem.setProductName(
                    product.getName()
            );

            orderItem.setImageUrl(
                    product.getImageUrl()
            );

            orderItem.setProductPrice(
                    product.getPrice()
            );

            orderItem.setDiscountAtPurchase(
                    product.getDiscount()
            );

            orderItem.setUnit(
                    product.getUnit()
            );

            orderItem.setQuantity(
                    item.getQuantity()
            );

            orderItem.setFinalPrice(
                    finalPrice
            );

            order.getItems()
                    .add(orderItem);

            // REDUCE STOCK

            product.setStock(

                    product.getStock()

                            -

                            item.getQuantity()
            );

            productRepo.save(product);
        }

        // GST

        double gstPercentage =
                settings.getGstPercentage();

        double gstAmount =
                (subtotal * gstPercentage)
                        / 100;

        // SHIPPING

        double shippingCharges =
                settings.getShippingCharges();

        // FINAL TOTAL

        double totalAmount =

                subtotal
                        + gstAmount
                        + shippingCharges;

        order.setSubtotal(
                subtotal
        );

        order.setGstPercentage(
                gstPercentage
        );

        order.setGstAmount(
                gstAmount
        );

        order.setShippingCharges(
                shippingCharges
        );

        order.setTotalAmount(
                totalAmount
        );

        // SAVE ORDER

        Order savedOrder =
                orderRepo.save(order);



        // MARK BASKET AS SAVED

        basket.setStatus(
                "SAVED"
        );

        basketRepo.save(
                basket
        );



        return savedOrder;
    }





    // ADMIN fetch all the orders

    public List<Order>
    getAllOrders() {

        return orderRepo
                .findAllByOrderByCreatedAtDesc();
    }

    // USER HISTORY

    public List<Order>
    getUserOrders(
            Long userId
    ) {

        User user =
                userRepo.findById(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                ));

        return orderRepo
                .findByUserOrderByCreatedAtDesc(
                        user
                );
    }

    // UPDATE STATUS

    @Transactional
    public Order updateStatus(

            Long orderId,

            String status

    ) {

        Order order =
                orderRepo.findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found"
                                ));

        order.setStatus(status);

        return orderRepo.save(order);
    }





// =====================================================================
//                  RECEIPT ENDPOINTS



    public ReceiptResponse
    getReceiptByOrderId(

            Long userId,

            Long orderId
    ) {

        Order order =
                orderRepo.findById(orderId)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Order not found"
                                )
                        );

        if (
                !order.getUser()
                        .getId()
                        .equals(userId)
        ) {

            throw new UnauthorizedException(
                    "Unauthorized access"
            );
        }


        List<ReceiptItemResponse>
                receiptItems =
                new ArrayList<>();


        double subtotal = 0;

        double totalDiscount = 0;

        double taxableAmount = 0;

        double totalGstAmount = 0;


        double gstPercentage =
                storeSettingsService
                        .getSettings()
                        .getGstPercentage();

        double shippingCharges =
                storeSettingsService
                        .getSettings()
                        .getShippingCharges();


        int totalItems =
                order.getItems().size();

        int totalQuantity = 0;


        for (
                OrderItem item :
                order.getItems()
        ) {

            double mrp =
                    item.getProductPrice();

            double discountPercent =
                    item.getDiscountAtPurchase();

            double discountAmount =
                    mrp * discountPercent / 100;

            double discountedPrice =
                    mrp - discountAmount;

            double itemSubtotal =
                    mrp * item.getQuantity();

            double itemDiscountTotal =
                    discountAmount
                            *
                            item.getQuantity();

            double itemTaxableAmount =
                    discountedPrice
                            *
                            item.getQuantity();

            double gstAmount =
                    roundValue(

                            itemTaxableAmount
                                    *
                                    gstPercentage
                                    / 100
                    );

            double finalTotal =
                    roundValue(

                            itemTaxableAmount
                                    +
                                    gstAmount
                    );


            subtotal += itemSubtotal;

            totalDiscount += itemDiscountTotal;

            taxableAmount += itemTaxableAmount;

            totalGstAmount += gstAmount;

            totalQuantity += item.getQuantity();


            ReceiptItemResponse
                    receiptItem =
                    new ReceiptItemResponse(

                            item.getProductName(),

                            item.getUnit(),

                            item.getQuantity(),

                            roundValue(mrp),

                            roundValue(discountPercent),

                            roundValue(discountedPrice),

                            roundValue(itemTaxableAmount),

                            roundValue(gstPercentage),

                            roundValue(gstAmount),

                            roundValue(finalTotal)
                    );

            receiptItems.add(
                    receiptItem
            );
        }


        double grandTotal =
                taxableAmount
                        +
                        totalGstAmount
                        +
                        shippingCharges;


        // ROUND SUMMARY VALUES

        subtotal =
                roundValue(subtotal);

        totalDiscount =
                roundValue(totalDiscount);

        taxableAmount =
                roundValue(taxableAmount);

        totalGstAmount =
                roundValue(totalGstAmount);

        shippingCharges =
                roundValue(shippingCharges);

        grandTotal =
                roundValue(grandTotal);


        ReceiptResponse response =
                new ReceiptResponse();


        // STORE INFO

        response.setStoreName(
                "Balaji Distributor System"
        );

        response.setStoreAddress(
                "123 Wholesale Market, Business District, Mumbai, Maharashtra - 400001"
        );

        response.setStorePhone(
                "+91 9876543210"
        );

        response.setStoreEmail(
                "contact@balajidistributors.in"
        );

        response.setStoreGstNumber(
                "27AAAAA0000A1Z5"
        );


        // ORDER INFO

        response.setOrderId(
                order.getId()
        );

        response.setInvoiceNumber(

                "INV-"

                        +

                        order.getCreatedAt()
                                .getYear()

                        +

                        "-"

                        +

                        order.getId()
        );

        response.setOrderDate(
                order.getCreatedAt()
        );

        response.setPaymentMethod(
                order.getPaymentMethod()
        );

        response.setOrderStatus(
                order.getStatus()
        );

        response.setPaymentStatus(

                order.getPaymentMethod()
                        .equalsIgnoreCase("UPI")

                        ?

                        "PAID"

                        :

                        "UNPAID"
        );


        // CUSTOMER INFO

        response.setCustomerName(
                order.getUser().getUsername()
        );

        response.setShopName(
                order.getUser().getShopname()
        );

        response.setCustomerMobile(
                order.getUser().getMobileno()
        );

        response.setCustomerGstNumber(
                order.getUser().getGstnumber()
        );


        // SHIPPING INFO

        response.setAddressLine1(
                order.getAddressLine1()
        );

        response.setAddressLine2(
                order.getAddressLine2()
        );

        response.setCity(
                order.getCity()
        );

        response.setState(
                order.getState()
        );

        response.setPincode(
                order.getPincode()
        );


        // ITEMS

        response.setItems(
                receiptItems
        );


        // SUMMARY

        response.setSubtotal(
                subtotal
        );

        response.setTotalDiscount(
                totalDiscount
        );

        response.setTaxableAmount(
                taxableAmount
        );

        response.setGstPercentage(
                roundValue(gstPercentage)
        );

        response.setTotalGstAmount(
                totalGstAmount
        );

        response.setShippingCharges(
                shippingCharges
        );

        response.setGrandTotal(
                grandTotal
        );

        response.setTotalItems(
                totalItems
        );

        response.setTotalQuantity(
                totalQuantity
        );


        return response;
    }



    private double roundValue(
            double value
    ) {

        return Math.round(
                value * 100.0
        ) / 100.0;
    }



}