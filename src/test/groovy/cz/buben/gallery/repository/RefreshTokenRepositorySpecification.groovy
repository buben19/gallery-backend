package cz.buben.gallery.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.CrudRepository
import spock.lang.Specification

@DataJpaTest
class RefreshTokenRepositorySpecification extends Specification {

    @Autowired
    RefreshTokenRepository refreshTokenRepository

    def "context loads"() {
        expect:
        refreshTokenRepository
    }

    def "implements CrudRepository"() {
        expect:
        refreshTokenRepository instanceof CrudRepository
    }
}
