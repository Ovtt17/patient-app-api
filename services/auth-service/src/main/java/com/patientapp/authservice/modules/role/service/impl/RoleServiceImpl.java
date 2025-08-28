package com.patientapp.authservice.modules.role.service.impl;

import com.patientapp.authservice.modules.role.entity.Role;
import com.patientapp.authservice.common.handler.exceptions.RoleNotFoundException;
import com.patientapp.authservice.modules.role.repository.RoleRepository;
import com.patientapp.authservice.modules.role.service.interfaces.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role findByNameOrThrow(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException(name + " no encontrado."));
    }
}
