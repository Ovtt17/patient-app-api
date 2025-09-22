package com.patientapp.patientservice.modules.patient.repository;

import com.patientapp.patientservice.modules.patient.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {
    Page<Patient> findAllByActiveTrue(Pageable pageable);
    Optional<Patient> findByIdAndActiveTrue(UUID id);
    Optional<Patient> findByUserIdAndActiveTrue(UUID userId);
}
