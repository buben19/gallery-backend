package cz.buben.sre.storage

import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.util.FileSystemUtils
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

class FileSystemStorageServiceSpecification extends Specification {

    Path root

    FileSystemStorageService service

    def setup() {
        root = Files.createTempDirectory("upload-")
        service = new FileSystemStorageService(root)
        service.init()
    }

    def cleanup() {
        FileSystemUtils.deleteRecursively(root)
    }

    def "temporary directory exists"() {
        expect:
        root
        Files.exists(root)
        Files.isDirectory(root)
        Files.walk(root).collect(Collectors.toList()) == [root]
    }

    def "save file"() {
        given:
        def file = new MockMultipartFile(
                "foo",
                "foo.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "test content".getBytes())

        when:
        service.store(file)

        then:
        Files.walk(root).collect(Collectors.toList()) == [root, root.resolve("foo.txt")]
    }
}
