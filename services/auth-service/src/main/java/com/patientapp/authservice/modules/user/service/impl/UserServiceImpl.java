package com.patientapp.authservice.modules.user.service.impl;

import com.patientapp.authservice.common.handler.exceptions.UserNotFoundException;
import com.patientapp.authservice.common.utils.NullSafe;
import com.patientapp.authservice.modules.role.entity.Role;
import com.patientapp.authservice.modules.role.enums.Roles;
import com.patientapp.authservice.modules.user.dto.UserRequestDTO;
import com.patientapp.authservice.modules.user.dto.UserResponseDTO;
import com.patientapp.authservice.modules.user.entity.User;
import com.patientapp.authservice.modules.user.mapper.UserMapper;
import com.patientapp.authservice.modules.user.repository.UserRepository;
import com.patientapp.authservice.modules.user.service.interfaces.UserService;
import com.patientapp.authservice.modules.user.updater.UserRoleUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final List<UserRoleUpdater> updaters;

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public UserResponseDTO getUserById(UUID id) {
        User user = findByIdOrThrow(id);
        return userMapper.toUserResponseDTO(user);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByIdOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado."));
    }

    @Override
    public User findByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado."));
    }

    @Override
    public User findByUsernameOrEmailOrThrow(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone.trim());
    }

    @Override
    @Transactional
    public UserResponseDTO update(UUID userId, UserRequestDTO request) {
        User user = findByIdOrThrow(userId);
        user.setFirstName(NullSafe.ifNotBlankOrNull(request.firstName()));
        user.setLastName(NullSafe.ifNotBlankOrNull(request.lastName()));
        user.setPhone(NullSafe.ifNotBlankOrNull(request.phone()));
        user.setGender(request.gender());
        user.setBiography(NullSafe.ifNotBlankOrNull(request.bio()));

        User userUpdated = userRepository.save(user);

        for (Role role : user.getRoles()) {
            updaters.stream()
                    .filter(u -> u.supports(Roles.valueOf(role.getName())))
                    .forEach(u -> u.updateUser(userUpdated, request));
        }

        return userMapper.toUserResponseDTO(userUpdated);
    }
}
