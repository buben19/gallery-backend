package cz.buben.gallery

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class GalleryApplicationSpecification extends Specification {

    def "context loads"() {
        expect:
        true
    }
}
