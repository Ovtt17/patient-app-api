package com.patientapp.doctorservice.modules.doctor.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    HOMBRE(1, "Hombre"),
    MUJER(2, "Mujer"),
    ;

    private final Integer id;
    private final String value;
}