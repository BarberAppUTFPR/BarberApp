package com.barberApp.Api.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateDTO(
        String name,
        String email,
        String phone,
        String password
) {
}
