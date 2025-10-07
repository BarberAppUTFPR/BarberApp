package com.barberApp.Api.user_cases.interfaces.user;


import com.barberApp.Api.dtos.RegisterDTO;

public interface UserEmailValidator {
    void validate(RegisterDTO registerDTO);
}
