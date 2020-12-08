package cz.buben.gallery;

import cz.buben.gallery.model.User;
import cz.buben.gallery.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@Profile("!test")
@Component
@AllArgsConstructor
public class AdminUserRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

    @Override
    public void run(String... args) throws Exception {
        Optional<User> adminOptional = this.userRepository.findByLogin("admin");
        if (adminOptional.isPresent()) {
            log.info("Admin user already exists.");
        } else {
            User admin = this.userRepository.save(User.builder()
                    .login("admin")
                    .password(this.passwordEncoder.encode("admin"))
                    .email("admin@localhost")
                    .created(Instant.now(this.clock))
                    .enabled(true)
                    .build());
            log.info("Admin user created: {}", admin);
        }
    }
}
