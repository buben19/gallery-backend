package cz.buben.gallery.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.CrudRepository
import spock.lang.Ignore
import spock.lang.Specification

@DataJpaTest
class RoleRepositorySpecification extends Specification {

    @Autowired
    RoleRepository roleRepository

    def "context loads"() {
        expect:
        roleRepository
    }

    def "implements CrudRepository"() {
        expect:
        roleRepository instanceof CrudRepository
    }

    @Ignore
    def "implement find by name"() {
        expect:
        false
    }
}
