package cz.buben.gallery.controller;

import cz.buben.gallery.dto.AuthenticationResponse;
import cz.buben.gallery.dto.LoginRequest;
import cz.buben.gallery.dto.RegistrationRequest;
import cz.buben.gallery.service.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegistrationRequest request) {
        try {
            this.authenticationService.signup(request);
            return ResponseEntity.ok("User registration successful");
        } catch (Throwable ex) {
            log.error("User registration failed: {}", ex.getMessage(), ex);
            return ResponseEntity.badRequest().body("User registration failed");
        }
    }

    @GetMapping("/verify/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        try {
            this.authenticationService.verifyAccount(token);
            return ResponseEntity.ok("User successfully verified");
        } catch (Throwable ex) {
            log.error("User verification failed: {}", ex.getMessage(), ex);
            return ResponseEntity.badRequest().body("User verification failed");
        }
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
        return this.authenticationService.login(loginRequest);
    }
}
