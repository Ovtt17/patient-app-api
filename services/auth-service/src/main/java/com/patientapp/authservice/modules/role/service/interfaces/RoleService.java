package com.patientapp.authservice.modules.role.service.interfaces;

import com.patientapp.authservice.modules.role.entity.Role;

public interface RoleService {
    Role findByNameOrThrow(String name);
}
