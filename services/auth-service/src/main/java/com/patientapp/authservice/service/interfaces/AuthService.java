package com.patientapp.authservice.service.interfaces;

import com.patientapp.authservice.dto.LoginRequest;
import com.patientapp.authservice.dto.RegisterRequest;
import com.patientapp.authservice.dto.UserResponseDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    String register(RegisterRequest request);
    String activateAccount(String token);
    UserResponseDTO login(LoginRequest request, HttpServletResponse response);
    String refresh(String refreshToken, HttpServletResponse response);
    String logout(HttpServletResponse response);
    UserResponseDTO getCurrentUser(String accessToken, String refreshToken, HttpServletResponse response);
}
