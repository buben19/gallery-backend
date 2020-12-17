package cz.buben.gallery.controller;

import cz.buben.gallery.dto.AuthenticationResponse;
import cz.buben.gallery.dto.LoginRequest;
import cz.buben.gallery.dto.RefreshTokenRequest;
import cz.buben.gallery.dto.RegistrationRequest;
import cz.buben.gallery.security.AuthenticationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            AuthenticationResponse login = this.authenticationService.login(loginRequest);
            log.debug("Login result: {}", login);
            return ResponseEntity.ok(login);
        } catch (BadCredentialsException ex) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Throwable ex) {
            log.error("Login error: {}", ex.getMessage(), ex);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            AuthenticationResponse refresh = this.authenticationService.refresh(refreshTokenRequest);
            return ResponseEntity.ok(refresh);
        } catch (Throwable ex) {
            // TODO: Catch token expiration.
            log.error("Refresh error: {}", ex.getMessage(), ex);
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
