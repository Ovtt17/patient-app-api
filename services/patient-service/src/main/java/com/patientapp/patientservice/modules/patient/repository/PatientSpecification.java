package com.patientapp.patientservice.modules.patient.repository;

import com.patientapp.patientservice.modules.patient.entity.Patient;
import org.springframework.data.jpa.domain.Specification;

public class PatientSpecification {
    public static Specification<Patient> filter(
            String name,
            String email,
            String phone
    ) {
        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (name != null && !name.isEmpty()) {
                String pattern = "%" + name.toLowerCase() + "%";
                predicates = cb.and(predicates, cb.or(
                        cb.like(cb.lower(root.get("firstName")), pattern),
                        cb.like(cb.lower(root.get("lastName")), pattern)
                ));
            }
            if (email != null && !email.isEmpty()) {
                predicates = cb.and(predicates, cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }
            if (phone != null && !phone.isEmpty()) {
                predicates = cb.and(predicates, cb.like(cb.lower(root.get("phone")), "%" + phone.toLowerCase() + "%"));
            }

            return predicates;
        };
    }
}
