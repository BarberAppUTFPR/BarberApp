package com.barberApp.Api.infra.exception_handler.exceptions;

public class HasToBeABarber extends RuntimeException {
    public HasToBeABarber(String message) {
        super(message);
    }
}
