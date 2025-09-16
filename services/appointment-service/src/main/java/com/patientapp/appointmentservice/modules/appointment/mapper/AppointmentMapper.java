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
                .appointmentDate(request.appointmentDate())
                .reason(NullSafe.ifNotBlankOrNull(request.reason()))
                .build();
    }

    public AppointmentResponseDTO toResponse(Appointment appointment) {
        return AppointmentResponseDTO.builder()
                .id(appointment.getId())
                .doctorId(appointment.getDoctorId())
                .patientId(appointment.getPatientId())
                .appointmentDate(appointment.getAppointmentDate())
                .endTime(NullSafe.ifPresentOrNull(appointment.getEndTime()))
                .estimatedDurationMinutes(NullSafe.ifPresentOrNull(appointment.getEstimatedDurationMinutes()))
                .reason(NullSafe.ifNotBlankOrNull(appointment.getReason()))
                .notes(NullSafe.ifNotBlankOrNull(appointment.getNotes()))
                .status(appointment.getStatus())
                .createdDate(appointment.getCreatedDate())
                .lastModifiedDate(appointment.getLastModifiedDate())
                .build();
    }
}
