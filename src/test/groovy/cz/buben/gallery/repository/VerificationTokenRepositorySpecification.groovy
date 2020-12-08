package cz.buben.gallery.repository

import cz.buben.gallery.model.User
import cz.buben.gallery.model.VerificationToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.CrudRepository
import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.Period
import java.time.ZoneId

@DataJpaTest
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

    def "implements CrudRepository"() {
        expect:
        verificationTokenRepository instanceof CrudRepository
    }

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
        token && token.id

        when:
        def findByToken = verificationTokenRepository.findByToken('token')

        then:
        findByToken.isPresent() && findByToken.get() == token
    }
}
