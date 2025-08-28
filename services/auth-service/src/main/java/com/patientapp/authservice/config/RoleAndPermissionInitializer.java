package com.patientapp.authservice.config;

import com.patientapp.authservice.modules.role.entity.Permission;
import com.patientapp.authservice.modules.role.entity.Role;
import com.patientapp.authservice.modules.role.enums.Permissions;
import com.patientapp.authservice.modules.role.repository.PermissionRepository;
import com.patientapp.authservice.modules.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.stream.Collectors;

import static com.patientapp.authservice.modules.role.enums.Roles.*;


@Configuration
@RequiredArgsConstructor
public class RoleAndPermissionInitializer {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Bean
    public CommandLineRunner initializeRolesAndPermissions() {
        return args -> {
            for (Permissions permission : Permissions.values()) {
                permissionRepository.findByName(permission.getName())
                        .orElseGet(() -> {
                            Permission permissionEntity = new Permission();
                            permissionEntity.setName(permission.getName());
                            return permissionRepository.save(permissionEntity);
                        });
            }

            var allPermissions = permissionRepository.findAll();
            Set<Permission> adminPermissions = Set.copyOf(allPermissions);
            Set<Permission> doctorPermissions = allPermissions.stream()
                    .filter(permission -> permission.getName().startsWith("doctor:"))
                    .collect(Collectors.toSet());
            Set<Permission> patientPermissions = Set.of();

            createRoleIfNotExists(ADMIN.name(), adminPermissions);
            createRoleIfNotExists(DOCTOR.name(), doctorPermissions);
            createRoleIfNotExists(PACIENTE.name(), patientPermissions);
        };
    }

    private void createRoleIfNotExists(String roleName, Set<Permission> permissions) {
        roleRepository.findByName(roleName).orElseGet(() -> {
            Role role = new Role();
            role.setName(roleName);
            role.setPermissions(permissions);
            return roleRepository.save(role);
        });
    }
}
