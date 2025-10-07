package com.barberApp.Api.services;

import com.barberApp.Api.dtos.GetBarbersDTO;
import com.barberApp.Api.dtos.RegisterDTO;
import com.barberApp.Api.dtos.UpdateDTO;
import com.barberApp.Api.models.Barber;
import com.barberApp.Api.repositories.BarberRepository;
import com.barberApp.Api.user_cases.interfaces.user.UserEmailValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BarberService {
    private final BarberRepository barberRepository;

    private final List<UserEmailValidator> userEmailValidators;

    public BarberService(BarberRepository barberRepository, List<UserEmailValidator> userEmailValidators) {
        this.barberRepository = barberRepository;
        this.userEmailValidators = userEmailValidators;
    }

    @Transactional
    public void Create(@Valid RegisterDTO dto) {
            userEmailValidators.forEach(v -> v.validate(dto));
            Barber newBarber = new Barber(dto);
            String encryptPassword = new BCryptPasswordEncoder().encode(dto.password());
            newBarber.setPassword(encryptPassword);
            barberRepository.save(newBarber);
    }

    @Transactional(readOnly = true)
    public Barber findById(UUID id) {
        return barberRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Barber with id " + id + " not found"));
    }

    // Read - List All Barbers
    @Transactional(readOnly = true)
    public List<GetBarbersDTO> findAll() {
            return barberRepository.findAll().stream().map(GetBarbersDTO::new).collect(Collectors.toList());
    }

    // Update Barber
    @Transactional
    public void update(UUID id, @Valid UpdateDTO dto) {
        Barber barber = barberRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Barber not found with ID: " + id)
        );
        barber.setName(dto.name());
        barber.setEmail(dto.email());
        barberRepository.save(barber);
    }

    // Delete Barber
    @Transactional
    public void delete(UUID id) {
        if (!barberRepository.existsById(id)) {
            throw new IllegalArgumentException("Barber not found with ID: " + id);
        }
        barberRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Barber findByEmail(String email) {
        Optional<Barber> barberOptional = barberRepository.findByEmail(email);
        return barberOptional.orElseThrow(() -> new EntityNotFoundException("Barber not found with email: " + email));
    }
}
