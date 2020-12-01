package cz.buben.sre.repository

import cz.buben.sre.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.CrudRepository
import spock.lang.Specification

import java.time.Instant

@SpringBootTest
class UserRepositorySpecification extends Specification {

    @Autowired
    UserRepository repository

    def setup() {
        repository.deleteAll()
    }

    def "context loads"() {
        expect:
        repository
    }

    def "implements CrudRepository"() {
        expect:
        repository instanceof CrudRepository
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
        user.setCreated(Instant.EPOCH)
        def save = repository.save(user)

        then:
        save && save.id
    }

    def "user can be found by login"() {
        when: "Create user."
        def user = repository.save(new User(
                firstName: 'name',
                lastName: 'surename',
                email: 'user@example.com',
                login: 'test-login',
                password: 'password',
                created: Instant.EPOCH
        ))

        then:
        user && user.getId()

        when: "Find user."
        def findByLogin = repository.findByLogin('test-login')

        then:
        findByLogin.isPresent()
        findByLogin.get() == user
    }

    def "two users with same login can't be created"() {
        when: "Create first user."
        def user = repository.save(new User(
                firstName: 'name',
                lastName: 'surename',
                email: 'user@example.com',
                login: 'test-login',
                password: 'password',
                created: Instant.EPOCH
        ))

        then:
        user && user.id

        when: "Create exactly same user."
        repository.save(new User(
                firstName: 'name',
                lastName: 'surename',
                email: 'user@example.com',
                login: 'test-login',
                password: 'password',
                created: Instant.EPOCH
        ))

        then:
        thrown(DataIntegrityViolationException)
    }
}
