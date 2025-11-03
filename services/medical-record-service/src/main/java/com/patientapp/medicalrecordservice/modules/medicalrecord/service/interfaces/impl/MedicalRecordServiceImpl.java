package com.patientapp.medicalrecordservice.modules.medicalrecord.service.interfaces.impl;

import com.patientapp.medicalrecordservice.modules.doctor.client.DoctorClient;
import com.patientapp.medicalrecordservice.modules.doctor.dto.DoctorResponse;
import com.patientapp.medicalrecordservice.modules.medicalrecord.dto.MedicalRecordRequest;
import com.patientapp.medicalrecordservice.modules.medicalrecord.dto.MedicalRecordResponse;
import com.patientapp.medicalrecordservice.modules.medicalrecord.entity.MedicalRecord;
import com.patientapp.medicalrecordservice.modules.medicalrecord.mapper.MedicalRecordMapper;
import com.patientapp.medicalrecordservice.modules.medicalrecord.repository.MedicalRecordRepository;
import com.patientapp.medicalrecordservice.modules.medicalrecord.service.interfaces.MedicalRecordService;
import com.patientapp.medicalrecordservice.modules.notification.MedicalRecordCreatedRequest;
import com.patientapp.medicalrecordservice.modules.notification.NotificationProducer;
import com.patientapp.medicalrecordservice.modules.patient.client.PatientClient;
import com.patientapp.medicalrecordservice.modules.patient.dto.PatientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository repository;
    private final MedicalRecordMapper mapper;
    private final PatientClient patientClient;
    private final DoctorClient doctorClient;
    private final NotificationProducer notificationProducer;

    @Override
    public MedicalRecordResponse create(MedicalRecordRequest request) {
        PatientResponse patient = patientClient.getById(request.patientId());
        DoctorResponse doctor = doctorClient.getById(request.doctorId());

        MedicalRecord entity = mapper.toEntity(request);
        MedicalRecord saved = repository.save(entity);

        MedicalRecordCreatedRequest notificationRequest = MedicalRecordCreatedRequest.builder()
                .id(saved.getId())
                .patientName(patient.firstName() + " " + patient.lastName())
                .patientEmail(patient.email())
                .doctorName(doctor.firstName() + " " + doctor.lastName())
                .doctorEmail(doctor.email())
                .appointmentId(saved.getAppointmentId())
                .weight(saved.getWeight())
                .height(saved.getHeight())
                .bloodType(saved.getBloodType())
                .allergies(saved.getAllergies())
                .chronicDiseases(saved.getChronicDiseases())
                .medications(saved.getMedications())
                .diagnostic(saved.getDiagnostic())
                .createdDate(saved.getCreatedDate())
                .build();

        notificationProducer.sendMedicalRecordCreatedEvent(notificationRequest);
        return mapper.toResponse(saved);
    }

    @Override
    public List<MedicalRecordResponse> findByPatient(UUID patientId) {
        return repository.findAllByPatientIdOrderByCreatedDateDesc(patientId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
