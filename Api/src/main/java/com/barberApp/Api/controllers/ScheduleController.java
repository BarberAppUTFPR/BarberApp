package com.barberApp.Api.controllers;

import com.barberApp.Api.dtos.ConfirmScheduleDTO;
import com.barberApp.Api.dtos.CreateScheduleDTO;
import com.barberApp.Api.dtos.ScheduleDTO;
import com.barberApp.Api.enums.ScheduleStatus;
import com.barberApp.Api.services.ScheduleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public ResponseEntity<?> getAllSchedules() {
        var schedules = scheduleService.findAll();
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ScheduleDTO>> getSchedulesByUserId(@PathVariable UUID userId) {
        var schedules = scheduleService.getSchedulesByUser(userId).stream().map(ScheduleDTO::new).toList();
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/barber/{barberId}")
    public ResponseEntity<List<ScheduleDTO>> getSchedulesByBarberAndStatus(@PathVariable UUID barberId, @RequestParam(required = false) String status) {
        List<ScheduleDTO> schedules;
        if (status == null) {
            schedules = scheduleService.getSchedulesByBarber(barberId).stream().map(ScheduleDTO::new).collect(Collectors.toList());
        } else {
            schedules = scheduleService.getSchedulesByBarberAndStatus(barberId, ScheduleStatus.valueOf(status)).stream().map(ScheduleDTO::new).collect(Collectors.toList());
        }
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirmSchedule(@RequestBody @Valid ConfirmScheduleDTO dto) {
        scheduleService.confirmSchedule(dto);
        return ResponseEntity.ok().build();
    }

}
