package com.barberApp.Api.dtos;


import com.barberApp.Api.models.Barber;

import java.util.UUID;

public record GetBarbersDTO(
        UUID uuid,
        String name
) {
    public GetBarbersDTO(Barber barber) {
        this(barber.getId() ,barber.getName());
    }
}
