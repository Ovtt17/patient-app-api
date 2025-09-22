package com.patientapp.authservice.modules.patient.updater;

import com.patientapp.authservice.common.utils.NullSafe;
import com.patientapp.authservice.modules.patient.client.PatientClient;
import com.patientapp.authservice.modules.patient.dto.PatientBasicInfoDTO;
import com.patientapp.authservice.modules.role.enums.Roles;
import com.patientapp.authservice.modules.user.dto.UserRequestDTO;
import com.patientapp.authservice.modules.user.entity.User;
import com.patientapp.authservice.modules.user.updater.UserRoleUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PatientUserUpdater implements UserRoleUpdater {
    private final PatientClient patientClient;

    @Override
    public void updateUser(User user, UserRequestDTO request) {
        PatientBasicInfoDTO patientUpdate = PatientBasicInfoDTO.builder()
                .firstName(NullSafe.ifNotBlankOrNull(request.firstName()))
                .lastName(NullSafe.ifNotBlankOrNull(request.lastName()))
                .phone(NullSafe.ifNotBlankOrNull(request.phone()))
                .gender(request.gender())
                .build();

        patientClient.updateBasicInfo(user.getId(), patientUpdate);
    }

    @Override
    public boolean supports(Roles role) {
        return role == Roles.PACIENTE;
    }
}
