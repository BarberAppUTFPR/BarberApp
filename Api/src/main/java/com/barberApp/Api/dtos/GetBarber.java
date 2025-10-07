package com.barberApp.Api.dtos;

import com.barberApp.Api.enums.User_Role;
import com.barberApp.Api.models.Barber;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record GetBarber(
        @NotBlank
        UUID uuid,
        @NotBlank
        String name,
        @NotBlank
        String email,
        @NotBlank
        String phone,
        @NotBlank
        User_Role role

) {
    public GetBarber(Barber barber) {
        this(barber.getId(), barber.getName(), barber.getEmail(), barber.getPhone(), barber.getUserRole());
    }
}
