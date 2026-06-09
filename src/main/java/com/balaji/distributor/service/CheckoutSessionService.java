package com.balaji.distributor.service;

import com.balaji.distributor.DTO.CheckoutSessionResponse;
import com.balaji.distributor.entity.*;
import com.balaji.distributor.exceptionHandler.PaymentException;
import com.balaji.distributor.exceptionHandler.ResourceNotFoundException;
import com.balaji.distributor.exceptionHandler.UnauthorizedException;
import com.balaji.distributor.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckoutSessionService {

    private final CheckoutSessionRepository
            sessionRepo;

    private final UserRepository
            userRepo;

    private final BasketRepository
            basketRepo;

    private final StoreSettingsService
            storeSettingsService;

    private final OrderService orderService;

    @Value("${app.payment-frontend-url}")
    private String paymentFrontendUrl;
    // CREATE SESSION



    @Transactional
    public CheckoutSessionResponse
    createSession(

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
                                )
                        );

        // BASKET

        Basket basket =
                basketRepo.findById(basketId)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Basket not found"
                                )
                        );

        if (
                !basket.getUser()
                        .getId()
                        .equals(userId)
        ) {

            throw new UnauthorizedException(
                    "Basket does not belong to user"
            );
        }

        // SETTINGS

        StoreSettings settings =
                storeSettingsService
                        .getSettings();

        // TOTALS

        double subtotal =

                basket.getItems()

                        .stream()

                        .mapToDouble(item -> {

                            Product product =
                                    item.getProduct();

                            double discountedPrice =

                                    product.getPrice()

                                            -

                                            (
                                                    product.getPrice()
                                                            *
                                                            product.getDiscount()
                                                            / 100
                                            );

                            return discountedPrice
                                    *
                                    item.getQuantity();
                        })

                        .sum();

        double gstPercentage =
                settings.getGstPercentage();

        double gstAmount =
                subtotal
                        *
                        gstPercentage
                        / 100;

        double shippingCharges =
                settings.getShippingCharges();

        double totalAmount =
                subtotal
                        +
                        gstAmount
                        +
                        shippingCharges;

        // SESSION

        CheckoutSession session =
                new CheckoutSession();

        session.setUser(user);

        session.setBasket(basket);

        session.setSubtotal(subtotal);

        session.setGstPercentage(
                gstPercentage
        );

        session.setGstAmount(
                gstAmount
        );

        session.setShippingCharges(
                shippingCharges
        );

        session.setTotalAmount(
                totalAmount
        );

        session.setPaymentMethod(
                paymentMethod
        );

        session.setPaymentStatus(
                "PENDING"
        );

        // SHIPPING

        session.setFullName(fullName);

        session.setMobile(mobile);

        session.setAddressLine1(
                addressLine1
        );

        session.setAddressLine2(
                addressLine2
        );

        session.setCity(city);

        session.setState(state);

        session.setPincode(pincode);

        // TIME

        session.setCreatedAt(
                LocalDateTime.now()
        );

        session.setExpiresAt(
                LocalDateTime.now()
                        .plusMinutes(5)
        );

        String txnId =
                "TXN"
                        +
                        System.currentTimeMillis();

        session.setTransactionId(
                txnId
        );

        // SAVE

        CheckoutSession saved =
                sessionRepo.save(session);

        // PAYMENT URL

        String paymentUrl =

                paymentFrontendUrl
                        +
                        "/payment/"
                        +
                        saved.getId();

        saved.setPaymentUrl(
                paymentUrl
        );

        sessionRepo.save(saved);

        // RESPONSE

        return mapResponse(saved);
    }



    // GET SESSION

    public CheckoutSessionResponse
    getSession(

            Long userId,

            Long sessionId
    ) {

        CheckoutSession session =
                sessionRepo.findById(sessionId)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Session not found"
                                )
                        );

        if (
                !session.getUser()
                        .getId()
                        .equals(userId)
        ) {

            throw new UnauthorizedException(
                    "Unauthorized access"
            );
        }

        // mark api expired after 5 min automatically
        if (
                session.getPaymentStatus()
                        .equals("PENDING")
                        &&
                        LocalDateTime.now()
                                .isAfter(
                                        session.getExpiresAt()
                                )
        ) {

            session.setPaymentStatus(
                    "EXPIRED"
            );

            sessionRepo.save(
                    session
            );
        }


        return mapResponse(
                session
        );
    }



    // PAY NOW

    @Transactional
    public CheckoutSessionResponse
    payNow(

            Long userId,

            Long sessionId
    ) {


        CheckoutSession session =
                sessionRepo.findById(sessionId)

                        .orElseThrow(() ->

                                new ResourceNotFoundException(
                                        "Session not found"
                                )
                        );

        if (
                !session.getUser()
                        .getId()
                        .equals(userId)
        ) {

            throw new UnauthorizedException(
                    "Unauthorized access"
            );
        }

        if (
                "PAID".equals(
                        session.getPaymentStatus()
                )
        ) {

            return mapResponse(
                    session
            );
        }


        if (
                "FAILED".equals(
                        session.getPaymentStatus()
                )
        ) {

            return mapResponse(
                    session
            );
        }


        if (

                LocalDateTime.now()

                        .isAfter(
                                session.getExpiresAt()
                        )

        ) {

            session.setPaymentStatus(
                    "EXPIRED"
            );

            sessionRepo.save(
                    session
            );

            return mapResponse(
                    session
            );

        }


        if (

                session.getOrderId()
                        != null

        ) {

            return mapResponse(
                    session
            );
        }




        // CREATE ORDER
        try {


            Order order =
                    orderService
                            .createOrder(

                                    session.getUser().getId(),

                                    session.getBasket().getId(),

                                    session.getPaymentMethod(),

                                    session.getFullName(),

                                    session.getMobile(),

                                    session.getAddressLine1(),

                                    session.getAddressLine2(),

                                    session.getCity(),

                                    session.getState(),

                                    session.getPincode()
                            );

            session.setPaymentStatus(
                    "PAID"
            );

            session.setOrderId(
                    order.getId()
            );

            session.setPaidAt(
                    LocalDateTime.now()
            );

            sessionRepo.save(
                    session
            );

        }
        catch (Exception e) {

            session.setPaymentStatus(
                    "FAILED"
            );

            sessionRepo.save(
                    session
            );

            return mapResponse(
                    session
            );
        }



        return mapResponse(session);
    }



    // RESPONSE MAPPER

    private CheckoutSessionResponse
    mapResponse(
            CheckoutSession session
    ) {

        CheckoutSessionResponse response =
                new CheckoutSessionResponse();

        response.setSessionId(
                session.getId()
        );

        response.setTotalAmount(
                session.getTotalAmount()
        );

        response.setPaymentMethod(
                session.getPaymentMethod()
        );

        response.setPaymentStatus(
                session.getPaymentStatus()
        );

        response.setTransactionId(
                session.getTransactionId()
        );

        response.setPaymentUrl(
                session.getPaymentUrl()
        );

        response.setCreatedAt(
                session.getCreatedAt()
        );

        response.setExpiresAt(
                session.getExpiresAt()
        );

        response.setPaidAt(
                session.getPaidAt()
        );

        response.setOrderId(
                session.getOrderId()
        );




        return response;
    }


    // mobile payment methods
    public CheckoutSessionResponse
    getPublicSession(

            Long id
    ) {

        CheckoutSession session =

                sessionRepo.findById(id)

                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Session not found"
                                )
                        );

        if (

                session.getExpiresAt()

                        .isBefore(
                                LocalDateTime.now()
                        )

        ) {

            throw new PaymentException(
                    "Session expired"
            );
        }

        return mapResponse(session);
    }


    @Transactional
    public CheckoutSessionResponse
    payPublic(
            Long id
    ) {

        CheckoutSession session =

                sessionRepo.findById(id)

                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "Session not found"
                                )
                        );

        if (

                session.getExpiresAt()

                        .isBefore(
                                LocalDateTime.now()
                        )

        ) {

            throw new PaymentException(
                    "Session expired"
            );
        }

        if (

                "PAID".equals(
                        session.getPaymentStatus()
                )

        ) {

            throw new PaymentException(
                    "Already paid"
            );
        }

        try {

            Order order =
                    orderService
                            .createOrder(

                                    session.getUser().getId(),

                                    session.getBasket().getId(),

                                    session.getPaymentMethod(),

                                    session.getFullName(),

                                    session.getMobile(),

                                    session.getAddressLine1(),

                                    session.getAddressLine2(),

                                    session.getCity(),

                                    session.getState(),

                                    session.getPincode()
                            );

            session.setPaymentStatus(
                    "PAID"
            );

            session.setOrderId(
                    order.getId()
            );

            session.setPaidAt(
                    LocalDateTime.now()
            );

            sessionRepo.save(
                    session
            );

        }
        catch (Exception e) {

            e.printStackTrace();

            session.setPaymentStatus(
                    "FAILED"
            );

            sessionRepo.save(
                    session
            );

            return mapResponse(
                    session
            );
        }

        return mapResponse(
                session
        );
    }

}

