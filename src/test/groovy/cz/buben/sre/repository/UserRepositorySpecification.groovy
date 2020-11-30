package cz.buben.sre.repository

import cz.buben.sre.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

@SpringBootTest
class UserRepositorySpecification extends Specification {

    @Autowired
    UserRepository repository

    Clock clock = Clock.fixed(Instant.EPOCH, ZoneId.systemDefault())

    def setup() {
        repository.deleteAll()
    }

    def "context loads"() {
        expect:
        repository
    }


    def "some fields are mandatory"() {
        given:
        def user = new User()

        when:
        repository.save(user)

        then:
        thrown(RuntimeException)

        when: "Set login field."
        user.setLogin('test-login')
        repository.save(user)

        then:
        thrown(RuntimeException)

        when: "Set password field."
        user.setPassword('password')
        repository.save(user)

        then:
        thrown(RuntimeException)

        when: "Set email field."
        user.setEmail('user@example.com')
        repository.save(user)

        then:
        thrown(RuntimeException)

        when: "Set created instant"
        user.setCreated(Instant.now(clock))
        def save = repository.save(user)

        then:
        save
        save.getId()
    }

    def "user can be found by login"() {
        when: "Create user."
        def user = repository.save(new User(
                firstName: 'name',
                lastName: 'surename',
                email: 'user@example.com',
                login: 'test-login',
                password: 'password',
                created: Instant.now(clock)
        ))

        then:
        user
        user.getId()

        when: "Find user."
        def findByLogin = repository.findByLogin('test-login')

        then:
        findByLogin.isPresent()
        findByLogin.get() == user
    }
}
