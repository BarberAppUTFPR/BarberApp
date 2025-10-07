package com.barberApp.Api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EmailDTO(
        @NotNull
        UUID user_id,
        @NotNull
        @Email
        String email_to,
        @NotNull
        String subject,
        String body
) {
}
