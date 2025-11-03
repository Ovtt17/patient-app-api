package com.patientapp.medicalrecordservice.modules.medicalrecord.repository;

import com.patientapp.medicalrecordservice.modules.medicalrecord.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findAllByPatientIdOrderByCreatedDateDesc(UUID patientId);
}
