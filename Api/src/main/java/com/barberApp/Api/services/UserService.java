package com.barberApp.Api.services;

import com.barberApp.Api.dtos.AuthenticationDTO;
import com.barberApp.Api.dtos.RegisterDTO;
import com.barberApp.Api.dtos.ResetPasswordDTO;
import com.barberApp.Api.dtos.UpdateDTO;
import com.barberApp.Api.enums.User_Role;
import com.barberApp.Api.infra.exception_handler.exceptions.EmailAlreadyExistsValidator;
import com.barberApp.Api.infra.exception_handler.exceptions.EmailIsntConfirmed;
import com.barberApp.Api.infra.security.JwtUtils;
import com.barberApp.Api.models.User;
import com.barberApp.Api.producers.UserProducer;
import com.barberApp.Api.repositories.UserRepository;
import com.barberApp.Api.user_cases.interfaces.user.UserDataValidator;
import com.barberApp.Api.user_cases.interfaces.user.UserEmailValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final UserProducer userProducer;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final List<UserEmailValidator> userEmailValidators;
    private final List<UserDataValidator> userDataValidators;


    public UserService(UserRepository userRepository, UserProducer userProducer, @Lazy AuthenticationManager authenticationManager, JwtUtils jwtUtils, List<UserEmailValidator> userEmailValidators, List<UserDataValidator> userDataValidators) {
        this.userRepository = userRepository;
        this.userProducer = userProducer;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userEmailValidators = userEmailValidators;
        this.userDataValidators = userDataValidators;
    }

    @Transactional
    public void Create(@Valid RegisterDTO dto) {
        try {
            userEmailValidators.forEach(v -> v.validate(dto));
            User newUser = new User(dto);
            String encryptPassword = new BCryptPasswordEncoder().encode(dto.password());
            newUser.setPassword(encryptPassword);
            saveConfirmation(newUser);
        } catch (EmailAlreadyExistsValidator e) {
            throw new EmailAlreadyExistsValidator(e.getMessage());
        }
    }

    @Transactional
    public void Update(UpdateDTO dto, UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + dto.email() + " not found."));

        if (dto.name() != null) user.setName(dto.name());
        if (dto.email() != null) user.setEmail(dto.email());
        if (dto.phone() != null) user.setPhone(dto.phone());
        if (dto.password() != null) user.setPassword(dto.password());

        userRepository.save(user);
    }

    @Transactional
    public void Delete(UUID id) {
        try {
            userRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("User with id " + id + " not found.");
        }
    }


    @Transactional
    public void saveConfirmation(User user) {
        userRepository.save(user);
        try {
            userProducer.sendConfirmationEmail(user);
        } catch (Exception e) {
            System.err.println("Failed to send confirmation email: " + e.getMessage());
        }
    }


    public List<User> findAll() {
        return userRepository.findAll().stream().filter(user -> user.getUserRole() == User_Role.USER)
                .collect(Collectors.toList());
    }


    @Scheduled(cron = "0 0 0 1/7 * ?")
    public void verifyEmailConfirmation() {
        List<User> users = userRepository.findAll().stream().filter(user -> !user.isEmailConfirmed()).toList();
        for (User user : users) {
                userProducer.sendConfirmationEmailVerifier(user);
        }
    }

    @Transactional
    public void confirmAccount(String token) {
        try {
            String userEmail = jwtUtils.validate(token);

            var user = userRepository.findByEmail(userEmail).orElseThrow(() -> new EntityNotFoundException("User with email " + userEmail + " not found."));

            if (user.isEmailConfirmed()) {
                throw new IllegalStateException("E-mail has already been confirmed");
            }

            user.setEmailConfirmed(true);

            userRepository.save(user);

        } catch (IllegalArgumentException | IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Token is invalid", e);
        }
    }


    public String login(AuthenticationDTO dto) throws AuthenticationException {
            userDataValidators.forEach(validator -> {
                try {
                    validator.validate(dto);
                } catch (EmailIsntConfirmed ex) {
                    userProducer.sendConfirmationEmail(userRepository.findByEmail(dto.email()).orElseThrow(() -> new EntityNotFoundException("User with email " + dto.email() + " not found.")));
                    throw new EmailIsntConfirmed("Email isnt confirmed");
                } catch (RuntimeException ex) {
                    throw new RuntimeException(ex);
                }
            });
            try {
                var usernameAndPassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
                var auth = this.authenticationManager.authenticate(usernameAndPassword);

                // Geração do token
                return jwtUtils.generateJwt((User) auth.getPrincipal());
            } catch (RuntimeException e) {
                throw new AuthenticationException("Auth failed");
            }
    }


    @Transactional
    public void requestConfirmation(UUID id) {
        var user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found."));
        userProducer.sendConfirmationEmail(user);
    }


    public void forgotPassword(User user) {
        if (user == null) return;
        userProducer.sendForgotPassword(user);
    }

    @Transactional
    public Map<String, String> resetPassword(String token, ResetPasswordDTO dto) {
        String newPassword = dto.new_password();

        if (newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException("New password is blank");
        }

        var subject = jwtUtils.validate(token).trim();
        var user = userRepository.findByEmail(subject).orElseThrow(() -> new EntityNotFoundException("User with email " + subject + " not found."));

        String encryptPassword = new BCryptPasswordEncoder().encode(newPassword);
        user.setPassword(encryptPassword);
        userRepository.save(user);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Email enviado. Caso exista uma conta com este email, as instruções serão enviadas.");
        return response;
    }

    @Transactional(readOnly = true)
    public User findUserByEmail(@NotBlank String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found."));
    }

    @Transactional(readOnly = true)
    public User findUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found."));
    }

}
