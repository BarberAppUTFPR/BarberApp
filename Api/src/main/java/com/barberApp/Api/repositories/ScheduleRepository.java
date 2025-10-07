package com.barberApp.Api.repositories;

import com.barberApp.Api.enums.ScheduleStatus;
import com.barberApp.Api.models.Barber;
import com.barberApp.Api.models.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    Boolean existsByBarberAndStartTime(Barber barber, LocalDateTime startTime);

    Page<Schedule> findByBarber_Id(UUID barberId, Pageable pageable);

    Page<Schedule> findSchedulesByBarber_idAndStatus(UUID barberId, ScheduleStatus status, Pageable pageable);

    Page<Schedule> findByUserId(UUID userId, Pageable pageable);

    @Query("SELECT s FROM Schedule s WHERE "
            + "s.barber = :barber AND "
            + "s.startTime = :startTime AND "
            + "s.status = :status")
    List<Schedule> findByBarberAndStartTimeAndStatus(
            @Param("barber") Barber barber,
            @Param("startTime") LocalDateTime startTime,
            @Param("status") ScheduleStatus status
    );
}
