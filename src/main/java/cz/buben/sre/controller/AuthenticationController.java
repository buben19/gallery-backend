package cz.buben.sre.controller;

import cz.buben.sre.dto.RegistrationRequest;
import cz.buben.sre.service.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(RegistrationRequest request) {
        try {
            this.service.signup(request);
            return ResponseEntity.ok("User registration successful");
        } catch (Throwable ex) {
            log.error("User registration failed: {}", ex.getMessage(), ex);
            return ResponseEntity.badRequest().body("User registration failed");
        }
    }
}
