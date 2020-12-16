package cz.buben.gallery.security

import cz.buben.gallery.data.NotificationEmail
import cz.buben.gallery.dto.AuthenticationResponse
import cz.buben.gallery.dto.LoginRequest
import cz.buben.gallery.dto.RegistrationRequest
import cz.buben.gallery.model.User
import cz.buben.gallery.model.VerificationToken
import cz.buben.gallery.repository.UserRepository
import cz.buben.gallery.repository.VerificationTokenRepository
import cz.buben.gallery.security.AuthenticationService
import cz.buben.gallery.security.JwtProvider
import cz.buben.gallery.service.MailService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.TestingAuthenticationToken
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Ignore
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
    AuthenticationManager authenticationManager = Mock()
    JwtProvider jwtProvider = Mock()
    RefreshTokenService refreshTokenService = Mock()
    AuthenticationService service = new AuthenticationService(passwordEncoder, userRepository,
            verificationTokenRepository, mailService, uuidSupplier, clock, authenticationManager, jwtProvider,
            refreshTokenService)

    def "user can signup"() {
        given:
        def uuid = UUID.randomUUID()

        when:
        service.signup(new RegistrationRequest(
                login: 'login',
                password: 'password',
                email: 'user@example.com'
        ))

        then: "Encode user password."
        1 * passwordEncoder.encode('password') >> 'encoded-password'

        then: "Create new user."
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

        and: "No other mock invocation."
        0 * _
    }

    def "account can be verified"() {
        given:
        def token = UUID.randomUUID().toString()

        when:
        service.verifyAccount(token)

        then:
        1 * verificationTokenRepository.findByToken(token) >> Optional.of(new VerificationToken(
                id: 1,
                token: token,
                user: new User(
                        id: 1,
                        login: 'login',
                        password: 'password',
                        email: 'user@example.com',
                        created: Instant.EPOCH
                ),
                expire: Instant.EPOCH + Period.ofDays(1)
        ))

        then:
        1 * userRepository.save(new User(
                id: 1,
                login: 'login',
                password: 'password',
                email: 'user@example.com',
                created: Instant.EPOCH,
                enabled: true
        ))

        then:
        1 * verificationTokenRepository.delete(new VerificationToken(
                id: 1,
                token: token,
                user: new User(
                        id: 1,
                        login: 'login',
                        password: 'password',
                        email: 'user@example.com',
                        created: Instant.EPOCH,
                        enabled: true
                ),
                expire: Instant.EPOCH + Period.ofDays(1)
        ))

        and: "No other mock invocation."
        0 * _
    }

    @Ignore
    def "user can login"() {
        when:
        def authenticationResponse = this.service.login(new LoginRequest(
                username: 'user',
                password: 'password'
        ))

        then:
        1 * authenticationManager.authenticate(new UsernamePasswordAuthenticationToken('user', 'password')) >>
                new TestingAuthenticationToken('user', 'password', 'ROLE_USER')

         1 * jwtProvider.generateToken(new TestingAuthenticationToken('user', 'password', 'ROLE_USER')) >>
                 new JwtProvider.JwtResult('token', Instant.EPOCH)

        authenticationResponse == new AuthenticationResponse(
                authenticationToken: 'token',
                username: 'user'
        )
    }

    def "get current user"() {
        given:
        def principal = org.springframework.security.core.userdetails.User.builder()
                .username('user')
                .password('password')
                .disabled(false)
                .accountExpired(false)
                .credentialsExpired(false)
                .authorities('test-authority')
                .build()
        def authentication = new UsernamePasswordAuthenticationToken(principal, null)
        SecurityContextHolder.getContext().setAuthentication(authentication)

        when:
        def user = service.getCurrentUser()

        then:
        1 * userRepository.findByLogin('user') >> Optional.of(new User(
                id: 1,
                login: 'user',
                password: 'password',
                email: 'user@example.com',
                created: Instant.EPOCH
        ))

        and:
        user
    }
}
