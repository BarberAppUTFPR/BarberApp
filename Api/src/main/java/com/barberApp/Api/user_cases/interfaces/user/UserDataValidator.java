package com.barberApp.Api.user_cases.interfaces.user;


import com.barberApp.Api.dtos.AuthenticationDTO;

public interface UserDataValidator {
    void validate(AuthenticationDTO authDTO);
}