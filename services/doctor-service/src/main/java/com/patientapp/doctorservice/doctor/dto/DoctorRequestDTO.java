package com.patientapp.doctorservice.doctor.dto;

import lombok.*;
import java.util.Set;
import java.util.UUID;

/**
 * DTO used to create or update a doctor in DoctorService
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String medicalLicense;
    private String officeNumber;
    private UUID userId; // UUID of user in Auth Service
    private Set<Integer> specialtyIds; // IDs of specialties
}