package com.patientapp.doctorservice.doctor.controller;

import com.patientapp.doctorservice.doctor.dto.DoctorRequestDTO;
import com.patientapp.doctorservice.doctor.entity.Doctor;
import com.patientapp.doctorservice.doctor.service.interfaces.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @Operation(summary = "Crear un nuevo doctor")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody DoctorRequestDTO request) {
        Doctor doctor = doctorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(doctor);
    }

    @Operation(summary = "Obtener todos los doctores activos")
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllActive() {
        List<Doctor> doctors = doctorService.getAllActive();
        return ResponseEntity.ok(doctors);
    }

    @Operation(summary = "Obtener un doctor por ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @Parameter(description = "UUID del doctor") @PathVariable UUID id
    ) {
        Doctor doctor = doctorService.getById(id);
        return ResponseEntity.ok(doctor);
    }

    @Operation(summary = "Actualizar información de un doctor")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @Parameter(description = "UUID del doctor") @PathVariable UUID id,
            @RequestBody DoctorRequestDTO request
    ) {
        Doctor updatedDoctor = doctorService.update(id, request);
        return ResponseEntity.ok(updatedDoctor);
    }

    @Operation(summary = "Desactivar un doctor")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deactivate(
            @Parameter(description = "UUID del doctor") @PathVariable UUID id
    ) {
        doctorService.deactivate(id);
        return ResponseEntity.ok("Doctor desactivado correctamente");
    }
}
