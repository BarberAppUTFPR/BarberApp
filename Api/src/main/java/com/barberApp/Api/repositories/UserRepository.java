package com.barberApp.Api.repositories;

import com.barberApp.Api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String adminEmail);

    Optional<User> findByEmail(String email);
}
