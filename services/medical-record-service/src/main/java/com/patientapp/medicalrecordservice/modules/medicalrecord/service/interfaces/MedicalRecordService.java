package com.patientapp.medicalrecordservice.modules.medicalrecord.service.interfaces;

import com.patientapp.medicalrecordservice.modules.medicalrecord.dto.MedicalRecordRequest;
import com.patientapp.medicalrecordservice.modules.medicalrecord.dto.MedicalRecordResponse;

import java.util.List;
import java.util.UUID;

public interface MedicalRecordService {
    MedicalRecordResponse create(MedicalRecordRequest request);
    List<MedicalRecordResponse> findByPatient(UUID patientId);
}
