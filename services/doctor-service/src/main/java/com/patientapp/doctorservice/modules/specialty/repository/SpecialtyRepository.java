package com.patientapp.doctorservice.modules.specialty.repository;

import com.patientapp.doctorservice.modules.specialty.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Integer> {
    // Find a specialty by name
    Optional<Specialty> findByName(String name);

    // Find multiple specialties by a list of ids
    List<Specialty> findByIdIn(List<Integer> ids);
}
