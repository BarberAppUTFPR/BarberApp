package com.barberApp.Api.user_cases.validations.user;

import com.christmann.haircut.schedule.dtos.RegisterDTO;
import com.christmann.haircut.schedule.infra.exception_handler.exceptions.EmailAlreadyExistsValidator;
import com.christmann.haircut.schedule.services.GenericUserService;
import com.christmann.haircut.schedule.user_cases.interfaces.user.UserEmailValidator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class EmailAlreadyExists implements UserEmailValidator {

    private final GenericUserService genericUserService;
    public EmailAlreadyExists(@Lazy GenericUserService genericUserService) {
        this.genericUserService = genericUserService;
    }

    @Override
    public void validate(RegisterDTO registerDTO) {
        var user = genericUserService.findByEmail(registerDTO.email());

        if (user != null && user.getEmail().equalsIgnoreCase(registerDTO.email())) {
            throw new EmailAlreadyExistsValidator("Email Already exists");
        }

    }
}
