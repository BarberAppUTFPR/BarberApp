package com.barberApp.Api.infra.exception_handler.exceptions;

public class EmailIsntConfirmed extends RuntimeException{
    public EmailIsntConfirmed (String message) {
        super(message);
    }
}
