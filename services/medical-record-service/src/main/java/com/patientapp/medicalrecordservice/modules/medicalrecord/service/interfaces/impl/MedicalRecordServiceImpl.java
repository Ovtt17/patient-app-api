package com.patientapp.medicalrecordservice.modules.medicalrecord.service.interfaces.impl;

import com.patientapp.medicalrecordservice.modules.medicalrecord.dto.MedicalRecordRequest;
import com.patientapp.medicalrecordservice.modules.medicalrecord.dto.MedicalRecordResponse;
import com.patientapp.medicalrecordservice.modules.medicalrecord.entity.MedicalRecord;
import com.patientapp.medicalrecordservice.modules.medicalrecord.mapper.MedicalRecordMapper;
import com.patientapp.medicalrecordservice.modules.medicalrecord.repository.MedicalRecordRepository;
import com.patientapp.medicalrecordservice.modules.medicalrecord.service.interfaces.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository repository;
    private final MedicalRecordMapper mapper;

    @Override
    public MedicalRecordResponse create(MedicalRecordRequest request) {
        MedicalRecord entity = mapper.toEntity(request);
        MedicalRecord saved = repository.save(entity);
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
