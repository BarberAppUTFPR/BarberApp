package com.barberApp.Api.user_cases.validations.schedule;

import com.barberApp.Api.dtos.CreateScheduleDTO;
import com.barberApp.Api.enums.User_Role;
import com.barberApp.Api.services.BarberService;
import com.barberApp.Api.services.UserService;
import com.barberApp.Api.user_cases.interfaces.schedule.ScheduleDataValidator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ScheduleCreationValidate implements ScheduleDataValidator {


    private final UserService userService;
    private final BarberService barberService;

    public ScheduleCreationValidate(@Lazy UserService userService, @Lazy BarberService barberService) {
        this.userService = userService;
        this.barberService = barberService;
    }

    @Override
    public void validateCreation(CreateScheduleDTO scheduleDTO) {

        var user = userService.findUserById(scheduleDTO.client_id());
        var barber = barberService.findById(scheduleDTO.barber_id());

        if (user.getUserRole() == User_Role.ADMIN) {
            throw new IllegalArgumentException("You cannot create a schedule as an administrator");
        }

        if (user.getUserRole() != User_Role.USER || barber.getUserRole() != User_Role.BARBER) {
            throw new IllegalArgumentException("ID's not valid");
        }
    }
}
