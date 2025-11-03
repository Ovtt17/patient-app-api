package com.patientapp.medicalrecordservice.modules.medicalrecord.controller;

import com.patientapp.medicalrecordservice.modules.medicalrecord.dto.MedicalRecordRequest;
import com.patientapp.medicalrecordservice.modules.medicalrecord.dto.MedicalRecordResponse;
import com.patientapp.medicalrecordservice.modules.medicalrecord.service.interfaces.MedicalRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {
    private final MedicalRecordService service;

    @PostMapping
    public ResponseEntity<MedicalRecordResponse> create(@RequestBody MedicalRecordRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<List<MedicalRecordResponse>> getByPatient(@PathVariable UUID id) {
        return ResponseEntity.ok(service.findByPatient(id));
    }
}
