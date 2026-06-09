package com.balaji.distributor.exceptionHandler;


public class ResourceNotFoundException
        extends RuntimeException {

    public ResourceNotFoundException(
            String message
    ) {

        super(message);
    }
}
