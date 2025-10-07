package com.barberApp.Api.models;

import com.barberApp.Api.dtos.RegisterDTO;
import com.barberApp.Api.enums.User_Role;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Barber extends User {


    public Barber() {}

    public Barber(RegisterDTO dto) {
        super(dto);
        this.setEmailConfirmed(true);
        this.setUserRole(User_Role.BARBER);
    }

}
