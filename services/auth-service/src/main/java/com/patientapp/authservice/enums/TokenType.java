package com.patientapp.authservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenType {
    ACCESS("access_token"),
    REFRESH("refresh_token"),
    ACTIVATION("activation_token")
    ;

    private final String type;
}
