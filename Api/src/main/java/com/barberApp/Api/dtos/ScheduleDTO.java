package com.barberApp.Api.dtos;

import com.barberApp.Api.enums.ScheduleStatus;
import com.barberApp.Api.models.Schedule;

import java.time.LocalDateTime;
import java.util.UUID;

public record ScheduleDTO(
        UUID schedule_id,
        String user_name,
        String barber_name,
        LocalDateTime start_date,
        ScheduleStatus schedule_status
) {
    public ScheduleDTO(Schedule schedule) {
        this(schedule.getId(), schedule.getUser().getName(), schedule.getBarber().getName(), schedule.getStartTime(), schedule.getStatus());
    }
}
