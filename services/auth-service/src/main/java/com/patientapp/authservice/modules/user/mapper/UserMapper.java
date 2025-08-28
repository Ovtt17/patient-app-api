package com.patientapp.authservice.modules.user.mapper;

import com.patientapp.authservice.modules.doctor.dto.DoctorRequestDTO;
import com.patientapp.authservice.modules.user.dto.UserResponseDTO;
import com.patientapp.authservice.modules.role.entity.Role;
import com.patientapp.authservice.modules.user.entity.User;
import com.patientapp.authservice.common.utils.NullSafe;
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
                .mustChangePassword(user.isMustChangePassword())
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
