package com.patientapp.doctorservice.modules.specialty.entity;

import com.patientapp.doctorservice.modules.doctor.entity.Doctor;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "specialties")
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 250)
    private String description;

    @ManyToMany(mappedBy = "specialties")
    private Set<Doctor> doctors;
}