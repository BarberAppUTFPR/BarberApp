package com.barberApp.Api.dtos;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateScheduleDTO(
        @NotNull UUID barber_id,
        @NotNull UUID client_id,
        @NotNull LocalDateTime start_time
) {
}
