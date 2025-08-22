package com.patientapp.authservice.service.interfaces;

import com.patientapp.authservice.entity.Role;

public interface RoleService {
    Role findByNameOrThrow(String name);
}
