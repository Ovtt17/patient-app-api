package com.patientapp.authservice.config;

import com.patientapp.authservice.modules.role.entity.Role;
import com.patientapp.authservice.modules.role.enums.Roles;
import com.patientapp.authservice.modules.role.service.interfaces.RoleService;
import com.patientapp.authservice.modules.user.entity.User;
import com.patientapp.authservice.modules.user.enums.AuthProvider;
import com.patientapp.authservice.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SuperAdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Value("${application.superadmin.email}")
    private String superAdminEmail;

    @Value("${application.superadmin.password}")
    private String superAdminPassword;

    @Transactional
    public void createSuperAdmin() {
        if (userRepository.existsByEmail(superAdminEmail)) {
            throw new IllegalArgumentException("User already exists");
        }

        // TODO: Change to ADMIN role to SUPER_ADMIN
        Role superAdminRole = roleService.findByNameOrThrow(Roles.ADMIN.name());

        if (superAdminRole == null) {
            throw new IllegalStateException("Super Admin role not found in the database");
        }

        User superAdmin = User.builder()
                .firstName("Super")
                .lastName("Admin")
                .email(superAdminEmail)
                .username(superAdminEmail)
                .password(passwordEncoder.encode(superAdminPassword))
                .enabled(true)
                .accountLocked(false)
                .roles(List.of(superAdminRole))
                .provider(AuthProvider.LOCAL)
                .mustChangePassword(false)
                .build();

        userRepository.save(superAdmin);
    }
}
