package com.barberApp.Api.dtos;


import com.barberApp.Api.models.User;

public record GetUsersDTO(
        String name,
        String email,
        String phone
) {

    public GetUsersDTO(User user) {
        this(user.getName(), user.getEmail(), user.getPhone());
    }
}
