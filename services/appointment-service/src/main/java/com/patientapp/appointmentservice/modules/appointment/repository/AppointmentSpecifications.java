package com.patientapp.appointmentservice.modules.appointment.repository;

import com.patientapp.appointmentservice.modules.appointment.dto.AppointmentFilterDTO;
import com.patientapp.appointmentservice.modules.appointment.entity.Appointment;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class AppointmentSpecifications {
    public static Specification<Appointment> filterAppointments(AppointmentFilterDTO filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.doctorId() != null) {
                predicates.add(cb.equal(root.get("doctorId"), filter.doctorId()));
            }

            if (filter.patientId() != null) {
                predicates.add(cb.equal(root.get("patientId"), filter.patientId()));
            }

            if (filter.status() != null) {
                predicates.add(cb.equal(root.get("status"), filter.status()));
            }

            if (filter.startDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("appointmentStart"), filter.startDate()));
            }

            if (filter.endDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("appointmentStart"), filter.endDate()));
            }

            if (predicates.isEmpty()) {
                return cb.conjunction();
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
