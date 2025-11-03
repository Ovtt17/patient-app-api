package com.patientapp.medicalrecordservice.modules.medicalrecord.mapper;

import com.patientapp.medicalrecordservice.modules.medicalrecord.dto.MedicalRecordRequest;
import com.patientapp.medicalrecordservice.modules.medicalrecord.dto.MedicalRecordResponse;
import com.patientapp.medicalrecordservice.modules.medicalrecord.entity.MedicalRecord;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class MedicalRecordMapper {

    public MedicalRecord toEntity(MedicalRecordRequest request) {
        if (request == null) return null;

        return MedicalRecord.builder()
                .patientId(request.patientId())
                .appointmentId(request.appointmentId())
                .weight(request.weight())
                .height(request.height())
                .bloodType(request.bloodType())
                .allergies(request.allergies())
                .chronicDiseases(request.chronicDiseases())
                .medications(request.medications())
                .diagnostic(request.diagnostic())
                .createdDate(Instant.now())
                .build();
    }

    public MedicalRecordResponse toResponse(MedicalRecord entity) {
        if (entity == null) return null;

        return new MedicalRecordResponse(
                entity.getId(),
                entity.getPatientId(),
                entity.getAppointmentId(),
                entity.getWeight(),
                entity.getHeight(),
                entity.getBloodType(),
                entity.getAllergies(),
                entity.getChronicDiseases(),
                entity.getMedications(),
                entity.getDiagnostic(),
                entity.getCreatedDate()
        );
    }
}
