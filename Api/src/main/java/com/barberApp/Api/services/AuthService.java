package com.barberApp.Api.services;

import com.barberApp.Api.repositories.BarberRepository;
import com.barberApp.Api.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BarberRepository barberRepository;

    public AuthService(UserRepository userRepository, BarberRepository barberRepository) {
        this.userRepository = userRepository;
        this.barberRepository = barberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .or(() -> barberRepository.findByEmail(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}