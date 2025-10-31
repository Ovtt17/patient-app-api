package com.patientapp.authservice.modules.user.mapper;

import com.patientapp.authservice.common.utils.NullSafe;
import com.patientapp.authservice.modules.role.entity.Role;
import com.patientapp.authservice.modules.user.dto.UserResponseDTO;
import com.patientapp.authservice.modules.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMapper {
    private UserResponseDTO.UserResponseDTOBuilder baseBuilder(User user) {
        return UserResponseDTO.builder()
                .id(NullSafe.ifPresentOrNull(user.getId()))
                .firstName(NullSafe.ifNotBlankOrNull(user.getFirstName()))
                .lastName(NullSafe.ifNotBlankOrNull(user.getLastName()))
                .email(NullSafe.ifNotBlankOrNull(user.getEmail()))
                .phone(NullSafe.ifNotBlankOrNull(user.getPhone()))
                .username(NullSafe.ifNotBlankOrNull(user.getUsername()))
                .profilePictureUrl(NullSafe.ifNotBlankOrNull(user.getProfilePicture()))
                .gender(user.getGender())
                .bio(NullSafe.ifNotBlankOrNull(user.getBiography()))
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .toList())
                .mustChangePassword(user.isMustChangePassword());
    }

    public UserResponseDTO toUserResponseDTO(User user) {
        return baseBuilder(user).build();
    }

    public UserResponseDTO toUserResponseDTO(User user, UUID patientId, UUID doctorId) {
        return baseBuilder(user)
                .patientId(patientId)
                .doctorId(doctorId)
                .build();
    }
}
