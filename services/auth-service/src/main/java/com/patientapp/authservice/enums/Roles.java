package com.patientapp.authservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Set;

import static com.patientapp.authservice.enums.Permissions.*;


@RequiredArgsConstructor
@Getter
public enum Roles {
    ADMIN(
            Set.of(
                    ADMIN_CREATE,
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    DOCTOR_CREATE,
                    DOCTOR_READ,
                    DOCTOR_UPDATE,
                    DOCTOR_DELETE
            )
    ),
    DOCTOR(
            Set.of(
                    DOCTOR_CREATE,
                    DOCTOR_READ,
                    DOCTOR_UPDATE,
                    DOCTOR_DELETE
            )
    ),
    PACIENTE(
            Collections.emptySet()
    );

    private final Set<Permissions> permissions;
}