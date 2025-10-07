package com.barberApp.Api.infra.exception_handler.exceptions;

public class EmailAlreadyExistsValidator extends RuntimeException{
    public EmailAlreadyExistsValidator(String message) {
        super(message);
    }
}