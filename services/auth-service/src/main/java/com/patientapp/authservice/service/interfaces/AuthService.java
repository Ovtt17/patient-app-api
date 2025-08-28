package com.patientapp.authservice.service.interfaces;

import com.patientapp.authservice.doctor.dto.DoctorCreatedDTO;
import com.patientapp.authservice.doctor.dto.DoctorRequestDTO;
import com.patientapp.authservice.dto.ChangePasswordRequest;
import com.patientapp.authservice.dto.LoginRequest;
import com.patientapp.authservice.dto.RegisterRequest;
import com.patientapp.authservice.dto.UserResponseDTO;
import com.patientapp.authservice.handler.exceptions.MustChangePasswordException;
import com.patientapp.authservice.handler.exceptions.TokenNotFoundException;
import com.patientapp.authservice.handler.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Service interface that defines authentication and user session operations.
 * <p>
 * Implementations should handle user registration, doctor registration,
 * account activation, login, token refresh, logout, and retrieval of the
 * currently authenticated user.
 * </p>
 */
public interface AuthService {
    /**
     * Registers a new patient account using the provided data.
     *
     * @param request the registration data, including credentials and profile information
     * @return a success message if the user is registered
     * @throws IllegalArgumentException if passwords do not match or the user already exists
     */
    String register(RegisterRequest request);

    /**
     * Registers a new doctor account and creates the corresponding doctor profile
     * in the doctor service.
     *
     * @param request the doctor registration data
     * @return a DTO containing the created doctor's email and a temporary password
     * @throws IllegalArgumentException if a user already exists with the given email or username
     */
    DoctorCreatedDTO registerDoctor(DoctorRequestDTO request);


    /**
     * Changes the password for the currently authenticated user.
     *
     * @param request  the change password request containing old and new passwords
     * @param response the HTTP response where auth cookies may be updated
     * @throws UnauthorizedException    if the user is not authenticated
     * @throws IllegalArgumentException if the old password is incorrect or new passwords do not match
     */
    void changePassword(ChangePasswordRequest request, HttpServletResponse response);


    /**
     * Activates a user account using the provided activation token.
     *
     * @param token the activation token
     * @return a success message if the account is activated
     * @throws TokenNotFoundException if the token does not exist
     * @throws RuntimeException       if the token is expired
     */
    String activateAccount(String token);

    /**
     * Authenticates a user and sets authentication cookies on the response.
     *
     * @param request  the login credentials
     * @param response the HTTP response where auth cookies will be set
     * @return the authenticated user information
     * @throws UnauthorizedException       if credentials are invalid
     * @throws MustChangePasswordException if the user must change their password
     */
    UserResponseDTO login(LoginRequest request, HttpServletResponse response);

    /**
     * Generates a new access token from the provided refresh token and adds it as a cookie.
     *
     * @param refreshToken the refresh token string
     * @param response     the HTTP response where the new access token cookie will be set
     * @return a message indicating the access token was refreshed
     * @throws UnauthorizedException if the refresh token is missing or invalid
     */
    String refresh(String refreshToken, HttpServletResponse response);

    /**
     * Clears authentication cookies to log the user out.
     *
     * @param response the HTTP response where cookies will be cleared
     * @return a message indicating the logout was successful
     */
    String logout(HttpServletResponse response);

    /**
     * Retrieves the currently authenticated user. If the access token is missing or expired,
     * the method may attempt to refresh it using the refresh token.
     *
     * @param accessToken  the current access token (may be blank)
     * @param refreshToken the refresh token used to obtain a new access token if needed
     * @param response     the HTTP response where a refreshed access token cookie may be set
     * @return the current authenticated user information
     * @throws UnauthorizedException if tokens are invalid
     */
    UserResponseDTO getCurrentUser(String accessToken, String refreshToken, HttpServletResponse response);
}
