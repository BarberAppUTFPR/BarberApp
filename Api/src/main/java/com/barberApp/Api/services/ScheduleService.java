package com.barberApp.Api.services;

import com.barberApp.Api.dtos.ConfirmScheduleDTO;
import com.barberApp.Api.dtos.CreateScheduleDTO;
import com.barberApp.Api.dtos.ScheduleDTO;
import com.barberApp.Api.enums.ScheduleStatus;
import com.barberApp.Api.models.Barber;
import com.barberApp.Api.models.Schedule;
import com.barberApp.Api.models.User;
import com.barberApp.Api.repositories.BarberRepository;
import com.barberApp.Api.repositories.ScheduleRepository;
import com.barberApp.Api.repositories.UserRepository;
import com.barberApp.Api.user_cases.interfaces.schedule.ScheduleDataValidator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;


@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final BarberRepository barberRepository;

    private final List<ScheduleDataValidator> scheduleDataValidators;

    public ScheduleService(ScheduleRepository scheduleRepository,
                           UserRepository userRepository,
                           BarberRepository barberRepository, List<ScheduleDataValidator> scheduleDataValidators) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.barberRepository = barberRepository;
        this.scheduleDataValidators = scheduleDataValidators;
    }

    @Transactional
    public void createSchedule(CreateScheduleDTO scheduleDTO) {
        scheduleDataValidators.forEach(v -> v.validateCreation(scheduleDTO));

        Barber barber = barberRepository.findById(scheduleDTO.barber_id())
                .orElseThrow(() -> new IllegalArgumentException("Barber not found"));


        User client = userRepository.findById(scheduleDTO.client_id())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        // Cria o agendamento
        Schedule schedule = new Schedule();
        schedule.setStartTime(scheduleDTO.start_time());
        schedule.setBarber(barber);
        schedule.setUser(client);

        // Salva no repositório
        barberRepository.save(barber);
        scheduleRepository.save(schedule);
    }

    @Transactional
    public void confirmSchedule(ConfirmScheduleDTO dto) {
        // Busca o agendamento a ser confirmado
        Schedule schedule = scheduleRepository.findById(dto.schedule_id())
                .orElseThrow(() -> new IllegalArgumentException("Agendamento não encontrado"));

        // Valida se o barbeiro corresponde
        if (!schedule.getBarber().getId().equals(dto.barber_id())) {
            throw new IllegalArgumentException("Barbeiro não corresponde ao agendamento");
        }

        // Confirma o agendamento atual
        schedule.setStatus(ScheduleStatus.CONFIRMED);
        scheduleRepository.save(schedule);

        // Busca conflitos (mesmo barbeiro, mesmo horário inicial e status pendente)
        List<Schedule> conflict = scheduleRepository.findByBarberAndStartTimeAndStatus(
                schedule.getBarber(),
                schedule.getStartTime(),
                ScheduleStatus.PENDING
        );

        // Remove agendamentos conflitantes (exceto o atual)
        conflict.removeIf(s -> s.getId().equals(schedule.getId())); // Garante que o atual não seja excluído

        scheduleRepository.deleteAll(conflict);
    }

    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesByBarber(UUID barberId) {
        return scheduleRepository.findByBarber_Id(barberId);
    }

    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesByBarberAndStatus(UUID barberId, ScheduleStatus status) {
        return scheduleRepository.findSchedulesByBarber_idAndStatus(barberId, status);
    }

    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesByUser(UUID userId) {
        return scheduleRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Stream<ScheduleDTO> findAll() {
        return scheduleRepository.findAll().stream().map(ScheduleDTO::new);
    }

    public Schedule getScheduleById(UUID scheduleId) {
        return scheduleRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
    }
}
