package cz.buben.gallery

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.nio.file.Path
import java.time.Clock
import java.util.function.Supplier

@SpringBootTest
class GalleryApplicationSpecification extends Specification {

    @Autowired
    Supplier<UUID> uuidSupplier

    @Autowired
    Clock clock

    @Autowired
    Path storageRoot

    def "context loads"() {
        expect:
        true
    }

    def "uuid supplier loads"() {
        expect:
        uuidSupplier
    }

    def "clock loads"() {
        expect:
        clock
    }

    def "storage root loads"() {
        expect:
        storageRoot
    }
}
