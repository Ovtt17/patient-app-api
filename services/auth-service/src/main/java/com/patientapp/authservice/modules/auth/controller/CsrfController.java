package com.patientapp.authservice.modules.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/csrf")
@Tag(name = "Csrf", description = "Csrf management")
public class CsrfController {
    @GetMapping("/token")
    public ResponseEntity<?> getToken(CsrfToken token) {
        if (token == null) {
            return ResponseEntity.badRequest().body("No token found");
        }
        return ResponseEntity.ok(token);
    }
}
