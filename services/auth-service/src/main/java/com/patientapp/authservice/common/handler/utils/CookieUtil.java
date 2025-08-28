package com.patientapp.authservice.common.handler.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patientapp.authservice.modules.user.dto.UserResponseDTO;
import com.patientapp.authservice.modules.user.entity.User;
import com.patientapp.authservice.modules.user.mapper.UserMapper;
import com.patientapp.authservice.security.JwtServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.patientapp.authservice.modules.token.enums.TokenType.ACCESS;
import static com.patientapp.authservice.modules.token.enums.TokenType.REFRESH;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

@Component
@RequiredArgsConstructor
public class CookieUtil {
    private final JwtServiceImpl jwtService;
    private final UserMapper userMapper;

    @Value("${application.front-end.domain}")
    private String frontendDomain;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public ResponseCookie createAccessTokenCookie(String token) {
        return createCookieWithHttpOnly(
                ACCESS.getType(),
                token,
                Duration.ofMillis(jwtExpiration)
        );
    }

    public ResponseCookie createRefreshTokenCookie(String token) {
        return createCookieWithHttpOnly(
                REFRESH.getType(),
                token,
                Duration.ofMillis(refreshExpiration)
        );
    }

    private ResponseCookie createCookieWithHttpOnly(String name, String value, Duration duration) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(duration)
                .path("/")
                .domain(frontendDomain)
                .build();
    }

    public ResponseCookie createCookieWithoutHttpOnly(String name, String value, Duration duration) {
        return ResponseCookie.from(name, value)
                .httpOnly(false)
                .secure(true)
                .sameSite("Strict")
                .maxAge(duration)
                .path("/")
                .domain(frontendDomain)
                .build();
    }

    public void setAuthCookies (
            final UserDetails user,
            final HttpServletResponse response
    ) {
        final String accessToken = jwtService.generateAccessToken(user);
        final String refreshToken = jwtService.generateRefreshToken(user);

        final ResponseCookie accessTokenCookie = createAccessTokenCookie(accessToken);
        final ResponseCookie refreshTokenCookie = createRefreshTokenCookie(refreshToken);
        final ResponseCookie userCookie = createUserCookie(user);

        response.addHeader(SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(SET_COOKIE, refreshTokenCookie.toString());
        response.addHeader(SET_COOKIE, userCookie.toString());
    }

    public ResponseCookie createUserCookie(final UserDetails userDetails) {
        try {
            User user = (User) userDetails;
            UserResponseDTO userResponse = userMapper.toUserResponseDTO(user);
            // Crear un mapa con los datos del usuario
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("username", userResponse.username());
            userInfo.put("email", userResponse.email());
            userInfo.put("profilePicture", NullSafe.ifNotBlankOrNull(userResponse.profilePictureUrl()));
            userInfo.put("roles", userResponse.roles());
            // Convertir a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(userInfo);

            // Usar URL-encoding
            String urlEncodedJson = URLEncoder.encode(json, StandardCharsets.UTF_8);

            return createCookieWithoutHttpOnly(
                    "user",
                    urlEncodedJson,
                    Duration.ofMillis(jwtExpiration)
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al serializar la información del usuario a JSON", e);
        }
    }

    public String getAccessTokenFromCookie(HttpServletRequest request) {
        return getTokenFromCookie(request, ACCESS.getType());
    }

    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        return getTokenFromCookie(request, REFRESH.getType());
    }

    private String getTokenFromCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    public void clearAuthCookies(HttpServletResponse response) {
        final ResponseCookie accessTokenCookie = createCookieWithHttpOnly(
                ACCESS.getType(),
                "",
                Duration.ofSeconds(0)
        );

        final ResponseCookie refreshTokenCookie = createCookieWithHttpOnly(
                REFRESH.getType(),
                "",
                Duration.ofSeconds(0)
        );

        final ResponseCookie userCookie = createCookieWithoutHttpOnly(
                "user",
                "",
                Duration.ofSeconds(0)
        );

        response.addHeader(SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(SET_COOKIE, refreshTokenCookie.toString());
        response.addHeader(SET_COOKIE, userCookie.toString());
    }
}
