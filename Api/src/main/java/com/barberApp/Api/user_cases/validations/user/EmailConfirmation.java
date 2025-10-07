package com.barberApp.Api.user_cases.validations.user;

import com.christmann.haircut.schedule.dtos.AuthenticationDTO;
import com.christmann.haircut.schedule.infra.exception_handler.exceptions.EmailIsntConfirmed;
import com.christmann.haircut.schedule.models.User;
import com.christmann.haircut.schedule.services.GenericUserService;
import com.christmann.haircut.schedule.user_cases.interfaces.user.UserDataValidator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class EmailConfirmation implements UserDataValidator {
    private final GenericUserService genericUserService;

    public EmailConfirmation(@Lazy GenericUserService genericUserService) {
        this.genericUserService = genericUserService;
    }

    @Override
    public void validate(AuthenticationDTO authDTO) {
        User user = genericUserService.findByEmail(authDTO.email());
        if (user == null) {
            throw new RuntimeException("E-mail does not exist");
        } else if (!user.isEmailConfirmed()) {
            throw new EmailIsntConfirmed("E-mail isn't confirmed");
        }
    }
}
