package com.patientapp.doctorservice.doctor.repository;

import com.patientapp.doctorservice.doctor.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    // Find a doctor by email
    Optional<Doctor> findByEmail(String email);

    // Find a doctor by userId from Auth Service
    Optional<Doctor> findByUserId(UUID userId);

    // Find all active doctors
    List<Doctor> findByActiveTrue();

    // Find doctors by specialty id
    List<Doctor> findBySpecialtiesId(Integer specialtyId);
}
