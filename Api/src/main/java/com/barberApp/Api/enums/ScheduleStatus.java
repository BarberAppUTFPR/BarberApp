package com.barberApp.Api.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ScheduleStatus {
    CONFIRMED("Confirmado"),
    CANCELLED("Cancelado"),
    COMPLETED("Concluído"),
    PENDING("Pendente");

    private final String status;

    ScheduleStatus(String status) {
        this.status = status;
    }

    @JsonValue
    public String getStatus() {
        return status; // Retorna o valor em português para o JSON
    }

    public static ScheduleStatus fromString(String value) {
        for (ScheduleStatus s : ScheduleStatus.values()) {
            if (s.status.equalsIgnoreCase(value) || s.name().equalsIgnoreCase(value)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}
