package com.patientapp.doctorservice.modules.doctor.repository;

import com.patientapp.doctorservice.modules.doctor.entity.Doctor;
import com.patientapp.doctorservice.modules.doctor.entity.DoctorUnavailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface DoctorUnavailabilityRepository extends JpaRepository<DoctorUnavailability, Integer> {
    List<DoctorUnavailability> findByDoctor(Doctor doctor);
    List<DoctorUnavailability> findByDoctorAndStartTimeBetween(Doctor doctor, Instant start, Instant end);
}
