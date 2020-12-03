package cz.buben.sre.storage

import spock.lang.Specification

import java.nio.file.Paths
import java.util.function.Supplier

class StoragePathGeneratorSpecification extends Specification {

    Supplier<UUID> uuidSupplier = Mock()
    StoragePathGenerator generator = new StoragePathGenerator(uuidSupplier)

    def "generate for file without extension"() {
        when:
        def path = generator.getStoragePath(Paths.get("a/b/c/foo"))

        then:
        1 * uuidSupplier.get() >> new UUID(1, 2)

        and:
        !path.isAbsolute() && path == Paths.get('00000000/0000/0001/0000/000000000002')
    }

    def "generate for file with extension"() {
        when:
        def path = generator.getStoragePath(Paths.get("a/b/c/foo.jpg"))

        then:
        1 * uuidSupplier.get() >> new UUID(1, 2)

        and:
        !path.isAbsolute() && path == Paths.get('00000000/0000/0001/0000/000000000002.jpg')
    }
}
