package com.patientapp.authservice.mapper;

import com.patientapp.authservice.dto.UserResponseDTO;
import com.patientapp.authservice.entity.Role;
import com.patientapp.authservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getProfilePicture(),
                user.getRoles().stream()
                        .map(Role::getName)
                        .toList()
        );
    }
}
