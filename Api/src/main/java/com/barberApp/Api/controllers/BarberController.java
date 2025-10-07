package com.barberApp.Api.controllers;

import com.barberApp.Api.dtos.GetBarber;
import com.barberApp.Api.dtos.GetBarbersDTO;
import com.barberApp.Api.infra.security.JwtUtils;
import com.barberApp.Api.services.BarberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/barber")
public class BarberController {

    private final BarberService barberService;
    private final JwtUtils jwtUtils;

    public BarberController(BarberService barberService, JwtUtils jwtUtils) {
        this.barberService = barberService;
        this.jwtUtils = jwtUtils;
    }
    
    @GetMapping
    public ResponseEntity<List<GetBarbersDTO>> getUsers() {
        var barbers = barberService.findAll();
        return ResponseEntity.ok(barbers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetBarber> getBarber(@PathVariable UUID id, HttpServletRequest request) {
        var barber = barberService.findById(id);
        var token = jwtUtils.retrieveToken(request);
        var barberEmail = jwtUtils.validate(token);
        var barberE = barberService.findByEmail(barberEmail);
        if (barber.equals(barberE)) {
            return ResponseEntity.ok(new GetBarber(barber));
        }
        return ResponseEntity.badRequest().build();
    }

}
