package cz.buben.sre.service

import cz.buben.sre.data.NotificationEmail
import cz.buben.sre.dto.RegisterRequest
import cz.buben.sre.model.User
import cz.buben.sre.model.VerificationToken
import cz.buben.sre.repository.UserRepository
import cz.buben.sre.repository.VerificationTokenRepository
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.Period
import java.time.ZoneId
import java.util.function.Supplier

class AuthenticationServiceSpecification extends Specification {

    PasswordEncoder passwordEncoder = Mock()
    UserRepository userRepository = Mock()
    VerificationTokenRepository verificationTokenRepository = Mock()
    MailService mailService = Mock()
    Supplier<UUID> uuidSupplier = Mock()
    Clock clock = Clock.fixed(Instant.ofEpochMilli(0), ZoneId.systemDefault())
    AuthenticationService service = new AuthenticationService(passwordEncoder, userRepository,
            verificationTokenRepository, mailService, uuidSupplier, clock)

    def "user can signup"() {
        given:
        def uuid = UUID.randomUUID()

        when:
        service.signup(new RegisterRequest(
                login: 'login',
                password: 'password',
                email: 'user@example.com'
        ))

        then:
        1 * passwordEncoder.encode('password') >> 'encoded-password'

        then:
        1 * userRepository.save(new User(
                login: 'login',
                password: 'encoded-password',
                email: 'user@example.com',
                created: Instant.now(clock)
        )) >> new User(
                id: 1,
                login: 'login',
                password: 'encoded-password',
                email: 'user@example.com',
                created: Instant.now(clock)
        )

        then:
        1 * uuidSupplier.get() >> uuid

        then:
        1 * verificationTokenRepository.save(new VerificationToken(
                user: new User(
                        id: 1,
                        login: 'login',
                        password: 'encoded-password',
                        email: 'user@example.com',
                        created: Instant.now(clock)
                ),
                token: uuid.toString(),
                expire: Instant.now(clock) + Period.ofDays(1)
        )) >> new VerificationToken(
                id: 1,
                user: new User(
                        id: 1,
                        login: 'login',
                        password: 'encoded-password',
                        email: 'user@example.com',
                        created: Instant.now(clock),
                ),
                token: uuid.toString(),
                expire: Instant.now(clock) + Period.ofDays(1)
        )

        then:
        1 * mailService.sendMail(new NotificationEmail(
                subject: 'Activate Account',
                recipient: 'user@example.com',
                body: uuid.toString()
        ))
    }
}
