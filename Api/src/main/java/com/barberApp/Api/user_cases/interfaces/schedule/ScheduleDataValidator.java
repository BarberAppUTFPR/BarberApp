package com.barberApp.Api.user_cases.interfaces.schedule;


import com.barberApp.Api.dtos.CreateScheduleDTO;

public interface ScheduleDataValidator {
    void validateCreation(CreateScheduleDTO schedule);
}
