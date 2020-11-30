package cz.buben.sre.repository

import cz.buben.sre.model.User
import cz.buben.sre.model.VerificationToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.Period
import java.time.ZoneId

@SpringBootTest
class VerificationTokenRepositorySpecification extends Specification {

    @Autowired
    UserRepository userRepository

    @Autowired
    VerificationTokenRepository verificationTokenRepository

    Clock clock = Clock.fixed(Instant.EPOCH, ZoneId.systemDefault())

    def setup() {
        userRepository.deleteAll()
        verificationTokenRepository.deleteAll()
    }

    def "context loads"() {
        expect:
        userRepository
        verificationTokenRepository
    }

    @Transactional
    def "find entity by token"() {
        when:
        def user = userRepository.save(new User(
                login: 'login',
                password: 'password',
                email: 'user@example.com',
                created: Instant.now(clock)
        ))

        def token = verificationTokenRepository.save(new VerificationToken(
                token: 'token',
                user: user,
                expire: Instant.now(clock) + Period.ofDays(1)
        ))

        then:
        token
        token.getId()

        when:
        def findByToken = verificationTokenRepository.findByToken('token')

        then:
        findByToken.isPresent()
        findByToken.get() == token
    }
}
