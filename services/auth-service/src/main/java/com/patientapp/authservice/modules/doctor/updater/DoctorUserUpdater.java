package com.patientapp.authservice.modules.doctor.updater;

import com.patientapp.authservice.common.utils.NullSafe;
import com.patientapp.authservice.modules.doctor.client.DoctorClient;
import com.patientapp.authservice.modules.doctor.dto.DoctorBasicInfoDTO;
import com.patientapp.authservice.modules.role.enums.Roles;
import com.patientapp.authservice.modules.user.dto.UserRequestDTO;
import com.patientapp.authservice.modules.user.entity.User;
import com.patientapp.authservice.modules.user.updater.UserRoleUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DoctorUserUpdater implements UserRoleUpdater {
    private final DoctorClient doctorClient;

    @Override
    public void updateUser(User user, UserRequestDTO request) {
        DoctorBasicInfoDTO doctorUpdate = DoctorBasicInfoDTO.builder()
                .firstName(NullSafe.ifNotBlankOrNull(request.firstName()))
                .lastName(NullSafe.ifNotBlankOrNull(request.lastName()))
                .phone(NullSafe.ifNotBlankOrNull(request.phone()))
                .gender(request.gender())
                .build();

        doctorClient.updateBasicInfo(user.getId(), doctorUpdate);
    }

    @Override
    public boolean supports(Roles role) {
        return role == Roles.DOCTOR;
    }
}
