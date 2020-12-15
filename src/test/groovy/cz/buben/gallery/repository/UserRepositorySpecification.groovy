package cz.buben.gallery.repository

import cz.buben.gallery.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.CrudRepository
import org.springframework.security.test.context.support.WithMockUser
import spock.lang.Specification

import javax.persistence.PersistenceException
import javax.validation.ConstraintViolationException
import java.time.Instant

@SuppressWarnings("GroovyAssignabilityCheck")
@DataJpaTest
class UserRepositorySpecification extends Specification {

    @Autowired
    UserRepository repository

    @Autowired
    TestEntityManager entityManager

    User user

    def setup() {
        user = repository.save(new User(
                firstName: 'name',
                lastName: 'surname',
                email: 'user@example.com',
                login: 'test-login',
                password: 'password',
                created: Instant.EPOCH
        ))
        entityManager.flush()
        entityManager.clear()
    }

    def "context loads"() {
        expect:
        repository
        entityManager
    }

    def "implements CrudRepository"() {
        expect:
        repository instanceof CrudRepository
    }

    def "database contains one user"() {
        when:
        def count = repository.count()

        then:
        count == 1

        when:
        def all = repository.findAll()

        then:
        all == [user]
    }

    def "login is required"() {
        when:
        user.setLogin(null)
        repository.save(user)
        entityManager.flush()

        then:
        thrown(ConstraintViolationException)
        entityManager.clear()
    }

    def "password is required"() {
        when:
        user.setPassword(null)
        repository.save(user)
        entityManager.flush()

        then:
        thrown(ConstraintViolationException)
        entityManager.clear()
    }

    def "email is required"() {
        when:
        user.setEmail(null)
        repository.save(user)
        entityManager.flush()

        then:
        thrown(ConstraintViolationException)
        entityManager.clear()
    }

    def "created instant is required"() {
        when:
        user.setCreated(null)
        repository.save(user)
        entityManager.flush()

        then:
        thrown(PersistenceException)
        entityManager.clear()
    }

    def "user can be found by login"() {
        when: "Find user."
        def findByLogin = repository.findByLogin('test-login')

        then:
        findByLogin.isPresent()
        findByLogin.get() == user
    }

    def "two users with same login can't be created"() {
        when: "Create exactly same user."
        repository.save(new User(
                firstName: 'name',
                lastName: 'surname',
                email: 'user@example.com',
                login: 'test-login',
                password: 'password',
                created: Instant.EPOCH
        ))
        entityManager.flush()

        then:
        thrown(PersistenceException)
        entityManager.clear()
    }
}
