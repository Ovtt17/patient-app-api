package com.patientapp.doctorservice.modules.doctor.repository;

import com.patientapp.doctorservice.modules.doctor.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    // Find a doctor by userId from Auth Service
    Optional<Doctor> findByUserIdAndActiveTrue(UUID userId);
    Page<Doctor> findAllByActiveTrue(Pageable pageable);
    List<Doctor> findAllByActiveTrue();
    Optional<Doctor> findByIdAndActiveTrue(UUID id);
}
