package com.barberApp.Api.controllers;

import com.barberApp.Api.dtos.ConfirmScheduleDTO;
import com.barberApp.Api.dtos.CreateScheduleDTO;
import com.barberApp.Api.dtos.ScheduleDTO;
import com.barberApp.Api.enums.ScheduleStatus;
import com.barberApp.Api.services.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<?> createSchedule(@RequestBody @Valid CreateScheduleDTO scheduleDTO) {
        scheduleService.createSchedule(scheduleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<Page<ScheduleDTO>> getAllSchedules(Pageable pageable) {
        var schedules = scheduleService.findAll(pageable);
        return ResponseEntity.ok(schedules.map(ScheduleDTO::new));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ScheduleDTO>> getSchedulesByUserId(@PathVariable UUID userId, Pageable pageable) {
        var schedules = scheduleService.getSchedulesByUser(userId, pageable);
        return ResponseEntity.ok(schedules.map(ScheduleDTO::new));
    }

    @GetMapping("/barber/{barberId}")
    public ResponseEntity<Page<ScheduleDTO>> getSchedulesByBarberAndStatus(@PathVariable UUID barberId, @RequestParam(required = false) String status, Pageable pageable) {
        Page<ScheduleDTO> schedules;
        if (status == null) {
            schedules = scheduleService.getSchedulesByBarber(barberId, pageable).map(ScheduleDTO::new);
        } else {
            schedules = scheduleService.getSchedulesByBarberAndStatus(barberId, ScheduleStatus.valueOf(status), pageable).map(ScheduleDTO::new);
        }
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirmSchedule(@RequestBody @Valid ConfirmScheduleDTO dto) {
        scheduleService.confirmSchedule(dto);
        return ResponseEntity.ok().build();
    }

}
