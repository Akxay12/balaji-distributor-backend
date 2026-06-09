package com.balaji.distributor.exceptionHandler;

import com.balaji.distributor.DTO.ErorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {



    // UNAUTHORIZED

    @ExceptionHandler(
            UnauthorizedException.class
    )

    public ResponseEntity<ErorResponse>
    handleUnauthorizedException(

            UnauthorizedException ex

    ) {

        ErorResponse error =
                new ErorResponse(

                        ex.getMessage(),

                        403,

                        LocalDateTime.now()
                );

        return new ResponseEntity<>(

                error,

                HttpStatus.FORBIDDEN
        );
    }


// BAD REQUEST

    @ExceptionHandler(
            BadRequestException.class
    )

    public ResponseEntity<ErorResponse>
    handleBadRequestException(

            BadRequestException ex

    ) {

        ErorResponse error =
                new ErorResponse(

                        ex.getMessage(),

                        400,

                        LocalDateTime.now()
                );

        return new ResponseEntity<>(

                error,

                HttpStatus.BAD_REQUEST
        );
    }


// PAYMENT EXCEPTION

    @ExceptionHandler(
            PaymentException.class
    )

    public ResponseEntity<ErorResponse>
    handlePaymentException(

            PaymentException ex

    ) {

        ErorResponse error =
                new ErorResponse(

                        ex.getMessage(),

                        409,

                        LocalDateTime.now()
                );

        return new ResponseEntity<>(

                error,

                HttpStatus.CONFLICT
        );
    }


// USER BLOCKED

    @ExceptionHandler(
            UserBlockedException.class
    )

    public ResponseEntity<ErorResponse>
    handleUserBlockedException(

            UserBlockedException ex

    ) {

        ErorResponse error =
                new ErorResponse(

                        ex.getMessage(),

                        403,

                        LocalDateTime.now()
                );

        return new ResponseEntity<>(

                error,

                HttpStatus.FORBIDDEN
        );
    }

    // RESOURCE NOT FOUND
    @ExceptionHandler(
            ResourceNotFoundException.class
    )

    public ResponseEntity<ErorResponse>
    handleResourceNotFoundException(

            ResourceNotFoundException ex

    ) {

        ErorResponse error =
                new ErorResponse(

                        ex.getMessage(),

                        404,

                        LocalDateTime.now()
                );

        return new ResponseEntity<>(

                error,

                HttpStatus.NOT_FOUND
        );
    }

    // GENERIC EXCEPTION

    @ExceptionHandler(Exception.class)

    public ResponseEntity<ErorResponse>
    handleGenericException(

            Exception ex

    ) {

        ErorResponse error =
                new ErorResponse(

                        ex.getMessage(),

                        500,

                        LocalDateTime.now()
                );

        return new ResponseEntity<>(

                error,

                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
