package com.patientapp.appointmentservice.modules.appointment.mapper;

import com.patientapp.appointmentservice.modules.appointment.dto.AppointmentRequestDTO;
import com.patientapp.appointmentservice.modules.appointment.dto.AppointmentResponseDTO;
import com.patientapp.appointmentservice.modules.appointment.entity.Appointment;
import com.patientapp.appointmentservice.common.utils.NullSafe;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public Appointment toEntity(AppointmentRequestDTO request) {
        return Appointment.builder()
                .doctorId(request.doctorId())
                .patientId(request.patientId())
                .appointmentStart(request.appointmentStart())
                .reason(NullSafe.ifNotBlankOrNull(request.reason()))
                .build();
    }

    public AppointmentResponseDTO toResponse(Appointment appointment) {
        return AppointmentResponseDTO.builder()
                .id(appointment.getId())
                .doctorId(appointment.getDoctorId())
                .patientId(appointment.getPatientId())
                .appointmentStart(appointment.getAppointmentStart())
                .appointmentEnd(NullSafe.ifPresentOrNull(appointment.getAppointmentEnd()))
                .plannedDurationMinutes(NullSafe.ifPresentOrNull(appointment.getPlannedDurationMinutes()))
                .reason(NullSafe.ifNotBlankOrNull(appointment.getReason()))
                .notes(NullSafe.ifNotBlankOrNull(appointment.getNotes()))
                .status(appointment.getStatus())
                .createdDate(appointment.getCreatedDate())
                .lastModifiedDate(appointment.getLastModifiedDate())
                .build();
    }
}
