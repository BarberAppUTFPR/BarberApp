package com.barberApp.Api.services;

import com.barberApp.Api.models.User;
import com.barberApp.Api.repositories.BarberRepository;
import com.barberApp.Api.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class GenericUserService {
    private final UserRepository userRepository;
    private final BarberRepository barberRepository;

    public GenericUserService(UserRepository userRepository, BarberRepository barberRepository) {
        this.userRepository = userRepository;
        this.barberRepository = barberRepository;
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.orElseGet(() -> barberRepository.findByEmail(email)
                .orElse(null));

    }
}
