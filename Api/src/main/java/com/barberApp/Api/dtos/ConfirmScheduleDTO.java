package com.barberApp.Api.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ConfirmScheduleDTO(
        @NotNull
        UUID schedule_id,
        @NotNull
        UUID barber_id
) {
}
