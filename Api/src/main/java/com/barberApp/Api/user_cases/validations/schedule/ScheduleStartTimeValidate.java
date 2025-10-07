package com.barberApp.Api.user_cases.validations.schedule;

import com.barberApp.Api.dtos.CreateScheduleDTO;
import com.barberApp.Api.enums.ScheduleStatus;
import com.barberApp.Api.infra.exception_handler.exceptions.ScheduleAlreadyExists;
import com.barberApp.Api.services.ScheduleService;
import com.barberApp.Api.user_cases.interfaces.schedule.ScheduleDataValidator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ScheduleStartTimeValidate implements ScheduleDataValidator {

    private final ScheduleService scheduleService;

    public ScheduleStartTimeValidate(@Lazy ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @Override
    public void validateCreation(CreateScheduleDTO schedule) {
        var schedules = scheduleService.getSchedulesByBarber(schedule.barber_id());
        schedules.forEach(s -> {
            if (s.getStartTime() == schedule.start_time() && s.getStatus() == ScheduleStatus.CONFIRMED) {
                throw new ScheduleAlreadyExists("schedule already exists");
            }
        });
    }
}
