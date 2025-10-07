package com.barberApp.Api.controllers;


import com.barberApp.Api.dtos.GetUsersDTO;
import com.barberApp.Api.infra.security.JwtUtils;
import com.barberApp.Api.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Stream;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final JwtUtils jwtUtils;

    public UserController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping
    public ResponseEntity<Stream<GetUsersDTO>> getUsers() {

        var users = userService.findAll();
        return ResponseEntity.ok(users.stream().map(GetUsersDTO::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUsersDTO> getUser(@PathVariable UUID id, HttpServletRequest request) {
        var token = jwtUtils.retrieveToken(request);
        var userEmail = jwtUtils.validate(token);
        var userByEmail = userService.findUserByEmail(userEmail);
        var user = userService.findUserById(id);
        if (user.equals(userByEmail)) {
            return ResponseEntity.ok(new GetUsersDTO(user));
        }
        return ResponseEntity.badRequest().build();
    }



}
