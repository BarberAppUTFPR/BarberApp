package com.barberApp.Api.repositories;

import com.barberApp.Api.models.Barber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BarberRepository extends JpaRepository<Barber, UUID> {
    Optional<Barber> findByEmail(String username);
}
