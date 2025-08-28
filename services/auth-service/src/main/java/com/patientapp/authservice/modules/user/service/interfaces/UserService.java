package com.patientapp.authservice.modules.user.service.interfaces;

import com.patientapp.authservice.modules.user.entity.User;

import java.util.UUID;

public interface UserService {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    User save(User user);
    User findByIdOrThrow(UUID id);
    User findByEmailOrThrow(String email);
    User findByUsernameOrEmailOrThrow(String username, String email);
}
