package com.patientapp.authservice.service.impl;

import com.patientapp.authservice.entity.Role;
import com.patientapp.authservice.handler.exceptions.RoleNotFoundException;
import com.patientapp.authservice.repository.RoleRepository;
import com.patientapp.authservice.service.interfaces.RoleService;
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
