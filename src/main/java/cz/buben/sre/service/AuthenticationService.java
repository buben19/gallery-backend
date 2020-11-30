package cz.buben.sre.service;

import cz.buben.sre.data.NotificationEmail;
import cz.buben.sre.dto.RegisterRequest;
import cz.buben.sre.model.User;
import cz.buben.sre.model.VerificationToken;
import cz.buben.sre.repository.UserRepository;
import cz.buben.sre.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.Period;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Slf4j
@Service
@AllArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final Supplier<UUID> uuidSupplier;
    private final Clock clock;

    public void signup(RegisterRequest request) {
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
        VerificationToken verificationToken = verificationTokenOptional.orElseThrow(
                () -> new RuntimeException("Verification token not found"));
        User user = verificationToken.getUser();
        user.enable();
        this.userRepository.save(user);
        this.verificationTokenRepository.delete(verificationToken);
    }
}
