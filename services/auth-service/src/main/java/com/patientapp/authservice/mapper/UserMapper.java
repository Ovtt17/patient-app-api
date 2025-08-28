package com.patientapp.authservice.mapper;

import com.patientapp.authservice.doctor.dto.DoctorRequestDTO;
import com.patientapp.authservice.dto.UserResponseDTO;
import com.patientapp.authservice.entity.Role;
import com.patientapp.authservice.entity.User;
import com.patientapp.authservice.utils.NullSafe;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    /**
     * Maps User entity to UserResponseDTO.
     * @param user the User entity to be mapped
     * @return the mapped UserResponseDTO
     */
    public UserResponseDTO toUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(NullSafe.ifPresentOrNull(user.getId()))
                .username(NullSafe.ifNotBlankOrNull(user.getUsername()))
                .email(NullSafe.ifNotBlankOrNull(user.getEmail()))
                .phone(NullSafe.ifNotBlankOrNull(user.getPhone()))
                .profilePictureUrl(NullSafe.ifNotBlankOrNull(user.getProfilePicture()))
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .toList())
                .build();
    }

    /**
     * Maps User entity to DoctorRequestDTO.
     * @param user the User entity to be mapped
     * @return the mapped DoctorRequestDTO
     */
    public DoctorRequestDTO toDoctorRequestDTO(User user) {
        return DoctorRequestDTO.builder()
                .firstName(user.getName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .userId(user.getId())
                .build();
    }
}
