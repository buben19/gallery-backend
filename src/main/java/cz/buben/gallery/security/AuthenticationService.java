package cz.buben.gallery.security;

import cz.buben.gallery.data.NotificationEmail;
import cz.buben.gallery.dto.AuthenticationResponse;
import cz.buben.gallery.dto.LoginRequest;
import cz.buben.gallery.dto.RefreshTokenRequest;
import cz.buben.gallery.dto.RegistrationRequest;
import cz.buben.gallery.model.User;
import cz.buben.gallery.model.VerificationToken;
import cz.buben.gallery.repository.UserRepository;
import cz.buben.gallery.repository.VerificationTokenRepository;
import cz.buben.gallery.service.MailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.Period;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@Transactional
@Service
@AllArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final Supplier<UUID> uuidSupplier;
    private final Clock clock;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public void signup(RegistrationRequest request) {
        User user = this.userRepository.save(User.builder()
                .login(request.getLogin())
                .password(this.encoder.encode(request.getPassword()))
                .email(request.getEmail())
                .created(Instant.now(this.clock))
                .build());
        log.debug("User created: {}", user);

        String token = this.uuidSupplier.get().toString();
        VerificationToken verificationToken = this.verificationTokenRepository.save(VerificationToken.builder()
                .user(user)
                .token(token)
                .expire(Instant.now(this.clock).plus(Period.ofDays(1)))
                .build());
        log.debug("Verification token created: {}", verificationToken);

        NotificationEmail notificationEmail = NotificationEmail.builder()
                .subject("Activate Account")
                .recipient(request.getEmail())
                .body(verificationToken.getToken())
                .build();
        log.debug("Notification email created: {}", notificationEmail);
        this.mailService.sendMail(notificationEmail);
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationTokenOptional = this.verificationTokenRepository.findByToken(token);
        log.debug("Found verification token: {}", verificationTokenOptional);
        VerificationToken verificationToken = verificationTokenOptional.orElseThrow(
                () -> new RuntimeException("Verification token not found"));
        User user = verificationToken.getUser();
        user.enable();
        this.userRepository.save(user);
        log.debug("User {} successfully enabled", user);
        this.verificationTokenRepository.delete(verificationToken);
        log.debug("Verification token {} deleted from database", verificationToken);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = this.jwtProvider.generateToken(authentication);
        return AuthenticationResponse.builder()
                .jwt(token)
                .build();
    }

    public AuthenticationResponse refresh(RefreshTokenRequest refreshTokenRequest) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return this.userRepository.findByLogin(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found: " + principal.getUsername()));
    }
}
