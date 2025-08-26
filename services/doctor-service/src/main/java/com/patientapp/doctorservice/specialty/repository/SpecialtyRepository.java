package com.patientapp.doctorservice.specialty.repository;

import com.patientapp.doctorservice.specialty.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpecialtyRepository extends JpaRepository<Specialty, Integer> {
    // Find a specialty by name
    Optional<Specialty> findByName(String name);

    // Find multiple specialties by a list of ids
    List<Specialty> findByIdIn(List<Integer> ids);
}
