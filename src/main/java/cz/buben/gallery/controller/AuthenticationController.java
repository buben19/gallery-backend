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

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@SuppressWarnings("unused")
@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegistrationRequest request) {
        this.authenticationService.signup(request);
        return ResponseEntity.ok("User registration successful");
    }

    @GetMapping("/verify/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        this.authenticationService.verifyAccount(token);
        return ResponseEntity.ok("User successfully verified");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            AuthenticationResponse login = this.authenticationService.login(loginRequest);
            log.debug("Login result: {}", login);
            return ResponseEntity.ok(login);
        } catch (BadCredentialsException ex) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            AuthenticationResponse refresh = this.authenticationService.refresh(refreshTokenRequest);
            return ResponseEntity.ok(refresh);
        } catch (SecurityException | EntityNotFoundException ex) {
            log.info("Can't refresh token: {}", ex.getMessage());
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        this.authenticationService.logout();
        return ResponseEntity.ok().body(null);
    }
}
