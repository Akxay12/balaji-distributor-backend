package com.balaji.distributor.exceptionHandler;

public class UnauthorizedException
        extends RuntimeException {

    public UnauthorizedException(
            String message
    ) {
        super(message);
    }
}
