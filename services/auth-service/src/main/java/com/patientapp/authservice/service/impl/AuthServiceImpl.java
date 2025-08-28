package com.patientapp.authservice.service.impl;

import com.patientapp.authservice.doctor.client.DoctorClient;
import com.patientapp.authservice.doctor.dto.DoctorCreatedDTO;
import com.patientapp.authservice.doctor.dto.DoctorRequestDTO;
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
import com.patientapp.authservice.utils.NullSafe;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
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
import java.util.UUID;

import static com.patientapp.authservice.enums.Roles.DOCTOR;
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
    private final DoctorClient doctorClient;

    @Override
    @Transactional
    public String register(RegisterRequest request) {
        verifyIfPasswordsMatch(request.password(), request.confirmPassword());
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
                .provider(AuthProvider.LOCAL)
                .roles(List.of(patientRole))
                .build();

        userService.save(user);
        // ToDo: send token via email
        return "Usuario registrado con éxito.";
    }

    @Override
    @Transactional
    public DoctorCreatedDTO registerDoctor(DoctorRequestDTO request) {
        verifyIfUserExists(request.email(), request.email());

        String tempPassword = generateTemporaryPassword();
        String passwordEncoded = passwordEncoder.encode(tempPassword);

        var doctorRole = roleService.findByNameOrThrow(DOCTOR.name());

        var user = User.builder()
                .firstName(NullSafe.ifNotBlankOrNull(request.firstName()))
                .lastName(NullSafe.ifNotBlankOrNull(request.lastName()))
                .username(NullSafe.ifNotBlankOrNull(request.username()))
                .email(NullSafe.ifNotBlankOrNull(request.email()))
                .phone(NullSafe.ifNotBlankOrNull(request.phone()))
                .accountLocked(false)
                .enabled(true) // enabled true because doctor will change the password at first login
                .password(passwordEncoded)
                .temporaryPassword(passwordEncoded)
                .roles(List.of(doctorRole))
                .provider(AuthProvider.LOCAL)
                .build();

        var userSaved = userService.save(user);

        DoctorRequestDTO doctorRequest = userMapper.toDoctorRequestDTO(userSaved);
        doctorClient.create(doctorRequest);
        // ToDo: send tempPassword via email
        return new DoctorCreatedDTO(userSaved.getEmail(), tempPassword);
    }

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
     * Verifies if the provided passwords match.
     * @param password the password
     * @param confirmPassword the confirmation password
     */
    private void verifyIfPasswordsMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }
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
