package com.patientapp.authservice.config.oauth2;

public record OAuthUserInfo(
        String email,
        String givenName,
        String familyName,
        String picture
) {
}
