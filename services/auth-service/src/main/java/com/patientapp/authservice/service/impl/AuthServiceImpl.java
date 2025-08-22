package com.patientapp.authservice.service.impl;

import com.patientapp.authservice.dto.LoginRequest;
import com.patientapp.authservice.dto.RegisterRequest;
import com.patientapp.authservice.dto.UserResponseDTO;
import com.patientapp.authservice.entity.Token;
import com.patientapp.authservice.entity.User;
import com.patientapp.authservice.enums.AuthProvider;
import com.patientapp.authservice.handler.exceptions.TokenNotFoundException;
import com.patientapp.authservice.handler.exceptions.UnauthorizedException;
import com.patientapp.authservice.mapper.UserMapper;
import com.patientapp.authservice.repository.TokenRepository;
import com.patientapp.authservice.service.interfaces.AuthService;
import com.patientapp.authservice.service.interfaces.JwtService;
import com.patientapp.authservice.service.interfaces.RoleService;
import com.patientapp.authservice.service.interfaces.UserService;
import com.patientapp.authservice.utils.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import static com.patientapp.authservice.enums.Roles.PACIENTE;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final CookieUtil cookieUtil;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserMapper userMapper;
    private final UserService userService;

    @Override
    public String register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }

        boolean userExists = userService.existsByEmail(request.getEmail())
                || userService.existsByUsername(request.getUsername());

        if (userExists) {
            throw new IllegalArgumentException("El usuario ya existe con el correo electrónico o nombre de usuario proporcionado.");
        }

        var patientRole = roleService.findByNameOrThrow(PACIENTE.name());

        var user = User.builder()
                .username(request.getUsername().trim())
                .email(request.getEmail().trim())
                .phone(request.getPhone().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(true) // ToDo: set to false for email verification
                .provider(AuthProvider.LOCAL)
                .roles(List.of(patientRole))
                .build();

        userService.save(user);
        // ToDo: send token via email
        return "Usuario registrado con éxito.";
    }

    @Override
    public String activateAccount(String token) {
        Token activationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Token no encontrado."));
        if (Instant.now().isAfter(activationToken.getExpiresAt())) {
            // ToDo: resend token
            throw new RuntimeException("El token ha expirado. Un nuevo token ha sido enviado al mismo correo electrónico");
        }
        User user = userService.findByIdOrThrow(activationToken.getUser().getId());
        user.setEnabled(true);
        userService.save(user);
        activationToken.setValidatedAt(Instant.now());
        tokenRepository.save(activationToken);
        return "Cuenta activada con éxito.";
    }

    @Override
    public UserResponseDTO login(LoginRequest request, HttpServletResponse response) {
        User user = userService.findByUsernameOrEmailOrThrow(request.getUsername(), request.getUsername());

        Authentication auth;
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new UnauthorizedException("El nombre de usuario o la contraseña son incorrectos.");
        }

        user = (User) auth.getPrincipal();
        cookieUtil.setAuthCookies(user, response);

        return userMapper.toUserResponseDTO(user);
    }

    @Override
    public String refresh(String refreshToken, HttpServletResponse response) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new UnauthorizedException("No se encontró el token de refresco.");
        }
        String username = jwtService.extractUsername(refreshToken);
        UserDetails user = userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new UnauthorizedException("Token de refresco inválido.");
        }

        final String newAccessToken = jwtService.generateAccessToken(user);
        final ResponseCookie accessTokenCookie = cookieUtil.createAccessTokenCookie(newAccessToken);

        response.addHeader(SET_COOKIE, accessTokenCookie.toString());

        return "Token de acceso actualizado.";
    }

    @Override
    public String logout(HttpServletResponse response) {
        cookieUtil.clearAuthCookies(response);
        return "Se ha cerrado la sesión correctamente.";
    }

    @Override
    public UserResponseDTO getCurrentUser(String accessToken, String refreshToken, HttpServletResponse response) {
        if (accessToken == null || accessToken.isBlank()) {
            refresh(refreshToken, response);
        }

        String email = jwtService.extractUsername(accessToken);
        User user = userService.findByEmailOrThrow(email);
        return userMapper.toUserResponseDTO(user);
    }

    private String generateAndSaveActivationToken(final User user) {
        final String generatedToken = generateActivationCode();

        final var token = Token.builder()
                .token(generatedToken)
                .createdAt(Instant.now())
                .expiresAt(Instant.from(LocalDateTime.now().plusMinutes(15)))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode() {
        final int tokenLength = 6;
        final String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < tokenLength; i++) {
            int randomIndex = random.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }
}
