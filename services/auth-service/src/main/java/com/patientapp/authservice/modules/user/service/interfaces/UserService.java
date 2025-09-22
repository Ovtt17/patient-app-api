package com.patientapp.authservice.modules.user.service.interfaces;

import com.patientapp.authservice.modules.user.dto.UserRequestDTO;
import com.patientapp.authservice.modules.user.dto.UserResponseDTO;
import com.patientapp.authservice.modules.user.entity.User;

import java.util.UUID;

public interface UserService {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    UserResponseDTO getUserById(UUID id);
    User save(User user);
    User findByIdOrThrow(UUID id);
    User findByEmailOrThrow(String email);
    User findByUsernameOrEmailOrThrow(String username, String email);
    boolean existsByPhone(String phone);
    UserResponseDTO update(UUID userId, UserRequestDTO request);
}
