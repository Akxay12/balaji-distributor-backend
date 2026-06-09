package com.balaji.distributor.exceptionHandler;

public class PaymentException
        extends RuntimeException {

    public PaymentException(
            String message
    ) {
        super(message);
    }
}