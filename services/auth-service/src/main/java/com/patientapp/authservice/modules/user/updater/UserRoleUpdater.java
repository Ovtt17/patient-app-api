package com.patientapp.authservice.modules.user.updater;

import com.patientapp.authservice.modules.role.enums.Roles;
import com.patientapp.authservice.modules.user.dto.UserRequestDTO;
import com.patientapp.authservice.modules.user.entity.User;

public interface UserRoleUpdater {
    void updateUser(User user, UserRequestDTO request);
    boolean supports(Roles role);
}
