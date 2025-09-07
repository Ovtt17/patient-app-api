package com.patientapp.authservice.modules.auth.service.impl;

import com.patientapp.authservice.modules.doctor.client.DoctorClient;
import com.patientapp.authservice.modules.doctor.dto.DoctorCreatedDTO;
import com.patientapp.authservice.modules.doctor.dto.DoctorRequestDTO;
import com.patientapp.authservice.modules.auth.dto.ChangePasswordRequest;
import com.patientapp.authservice.modules.auth.dto.LoginRequest;
import com.patientapp.authservice.modules.auth.dto.RegisterRequest;
import com.patientapp.authservice.modules.patient.client.PatientClient;
import com.patientapp.authservice.modules.user.dto.UserResponseDTO;
import com.patientapp.authservice.modules.token.entity.Token;
import com.patientapp.authservice.modules.user.entity.User;
import com.patientapp.authservice.modules.user.enums.AuthProvider;
import com.patientapp.authservice.common.handler.exceptions.TokenNotFoundException;
import com.patientapp.authservice.common.handler.exceptions.UnauthorizedException;
import com.patientapp.authservice.modules.user.mapper.UserMapper;
import com.patientapp.authservice.modules.token.repository.TokenRepository;
import com.patientapp.authservice.modules.auth.service.interfaces.AuthService;
import com.patientapp.authservice.security.JwtService;
import com.patientapp.authservice.modules.role.service.interfaces.RoleService;
import com.patientapp.authservice.modules.user.service.interfaces.UserService;
import com.patientapp.authservice.common.utils.CookieUtil;
import com.patientapp.authservice.common.utils.NullSafe;
import com.patientapp.authservice.common.utils.SecurityUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.UUID;

import static com.patientapp.authservice.modules.role.enums.Roles.DOCTOR;
import static com.patientapp.authservice.modules.role.enums.Roles.PACIENTE;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

/**
 * Default service implementation for authentication and user session operations.
 * Implements {@link AuthService}.
 */
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
    private final DoctorClient doctorClient;
    private final PatientClient patientClient;

    @Value("${application.front-end.url}")
    private String frontendUrl;

    /** {@inheritDoc} */
    @Override
    @Transactional
    public String register(RegisterRequest request) {
        verifyPasswordComparison(request.password(), request.confirmPassword());
        verifyIfUserExists(request.email(), request.username());

        var patientRole = roleService.findByNameOrThrow(PACIENTE.name());

        var user = User.builder()
                .firstName(NullSafe.ifNotBlankOrNull(request.firstName()))
                .lastName(NullSafe.ifNotBlankOrNull(request.lastName()))
                .username(NullSafe.ifNotBlankOrNull(request.username()))
                .email(NullSafe.ifNotBlankOrNull(request.email()))
                .phone(NullSafe.ifNotBlankOrNull(request.phone()))
                .password(passwordEncoder.encode(request.password()))
                .accountLocked(false)
                .enabled(true) // ToDo: set to false for email verification
                .mustChangePassword(false)
                .provider(AuthProvider.LOCAL)
                .roles(List.of(patientRole))
                .build();

        var userSaved = userService.save(user);
        var userId = patientClient.create(userSaved.getId());
        // ToDo: send token via email
        return "Usuario registrado con éxito.";
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public DoctorCreatedDTO registerDoctor(DoctorRequestDTO request) {
        verifyIfUserExists(request.email(), request.email());

        String tempPassword = generateTemporaryPassword();

        var doctorRole = roleService.findByNameOrThrow(DOCTOR.name());

        var user = User.builder()
                .firstName(NullSafe.ifNotBlankOrNull(request.firstName()))
                .lastName(NullSafe.ifNotBlankOrNull(request.lastName()))
                .username(NullSafe.ifNotBlankOrNull(request.username()))
                .email(NullSafe.ifNotBlankOrNull(request.email()))
                .phone(NullSafe.ifNotBlankOrNull(request.phone()))
                .accountLocked(false)
                .enabled(true) // enabled true because doctor will change the password at first login
                .password(passwordEncoder.encode(tempPassword))
                .mustChangePassword(true) // force to change password at first login
                .roles(List.of(doctorRole))
                .provider(AuthProvider.LOCAL)
                .build();

        var userSaved = userService.save(user);

        doctorClient.create(userSaved.getId());
        // ToDo: send tempPassword via email
        return new DoctorCreatedDTO(userSaved.getEmail(), tempPassword);
    }

    /** {@inheritDoc} */
    @Override
    public void changePassword(ChangePasswordRequest request, HttpServletResponse response) {
        String email = SecurityUtil.getAuthenticatedEmail()
                .orElseThrow(() -> new UnauthorizedException("Usuario no autenticado."));

        User user = userService.findByEmailOrThrow(email);

        // Verify old password matches the current password in DB
        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("La contraseña antigua es incorrecta.");
        }

        // Verify new password and confirmation match each other
        verifyPasswordComparison(request.newPassword(), request.confirmNewPassword());
        // Verify new password is different from old password
        boolean shouldMathOldAndNewPassword = false;
        verifyPasswordComparison(
                request.oldPassword(),
                request.newPassword(),
                shouldMathOldAndNewPassword
        );

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setMustChangePassword(false);
        userService.save(user);

        // Logout the user to force re-login with new password
        cookieUtil.clearAuthCookies(response);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
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

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    public String logout(HttpServletResponse response) {
        cookieUtil.clearAuthCookies(response);
        return "Se ha cerrado la sesión correctamente.";
    }

    /** {@inheritDoc} */
    @Override
    public UserResponseDTO getCurrentUser(String accessToken, String refreshToken, HttpServletResponse response) {
        if (accessToken == null || accessToken.isBlank()) {
            refresh(refreshToken, response);
        }

        String email = jwtService.extractUsername(accessToken);
        User user = userService.findByEmailOrThrow(email);
        return userMapper.toUserResponseDTO(user);
    }

    @Override
    public boolean validateToken(String token) {
        String username = jwtService.extractUsername(token);
        UserDetails user = userDetailsService.loadUserByUsername(username);
        return jwtService.isTokenValid(token, user);
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

    /**
     * Generates a secure temporary password.
     * @return A temporary password string of 8 characters.
     */
    private String generateTemporaryPassword() {
        // 🔹 Genera una contraseña de 10 caracteres segura
        return UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 8);
    }

    /**
     * Verifies that two passwords comply with the matching or non-matching condition.
     * @param password the main password
     * @param confirmPassword the confirmation password
     * @param shouldMatch true if passwords must match, false if they must be different
     */
    private void verifyPasswordComparison(
            String password,
            String confirmPassword,
            boolean shouldMatch
    ) {
        if (shouldMatch && !password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }
        if (!shouldMatch && password.equals(confirmPassword)) {
            throw new IllegalArgumentException("La nueva contraseña no puede ser igual a la anterior.");
        }
    }

    /**
     * Verifies that two passwords match.
     * @param password the main password
     * @param confirmPassword the confirmation password
     */
    private void verifyPasswordComparison(String password, String confirmPassword) {
        boolean shouldMatch = true;
        verifyPasswordComparison(password, confirmPassword, shouldMatch);
    }

    /**
     * Verifies if a user already exists with the given email or username.
     * @param email the email
     * @param username the username
     */
    private void verifyIfUserExists(String email, String username) {
        boolean userExists = userService.existsByEmail(email)
                || userService.existsByUsername(username);

        if (userExists) {
            throw new IllegalArgumentException("El usuario ya existe con el correo electrónico o nombre de usuario proporcionado.");
        }
    }

}
