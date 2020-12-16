package cz.buben.gallery.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.CrudRepository
import spock.lang.Specification

@DataJpaTest
class PrivilegeRepositorySpecification extends Specification {

    @Autowired
    PrivilegeRepository privilegeRepository

    def "context loads"() {
        expect:
        privilegeRepository
    }

    def "implements CrudRepository"() {
        expect:
        privilegeRepository instanceof CrudRepository
    }
}
