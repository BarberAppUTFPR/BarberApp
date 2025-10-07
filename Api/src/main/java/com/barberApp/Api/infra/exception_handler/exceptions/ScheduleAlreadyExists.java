package com.barberApp.Api.infra.exception_handler.exceptions;

public class ScheduleAlreadyExists extends RuntimeException {
    public ScheduleAlreadyExists(String message) {
        super(message);
    }
}
