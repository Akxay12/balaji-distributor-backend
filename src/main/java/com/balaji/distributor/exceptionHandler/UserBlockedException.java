package com.balaji.distributor.exceptionHandler;

public class UserBlockedException
        extends RuntimeException {

    public UserBlockedException(
            String message
    ) {
        super(message);
    }
}