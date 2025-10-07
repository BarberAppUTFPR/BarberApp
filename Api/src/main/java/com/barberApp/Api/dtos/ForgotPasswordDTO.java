package com.barberApp.Api.dtos;

import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordDTO(@NotBlank String email) {
}
