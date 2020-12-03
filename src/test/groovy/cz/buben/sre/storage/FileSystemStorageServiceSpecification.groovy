package cz.buben.sre.storage

import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.util.FileSystemUtils
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
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
        service.store(Paths.get('a/b/c/d/test'), file)

        then:
        Files.walk(root).collect(Collectors.toSet()) == [
                root,
                root.resolve("a"),
                root.resolve("a/b"),
                root.resolve("a/b/c"),
                root.resolve("a/b/c/d"),
                root.resolve("a/b/c/d/test")
        ] as Set
    }

    def "file outside of root can't be saved"() {
        given:
        def file = new MockMultipartFile(
                "foo",
                "foo.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "test content".getBytes())

        when:
        service.store(Paths.get('../test'), file)

        then:
        thrown(StorageException)
    }

    def "empty file can't be saved"() {
        given:
        def file = new MockMultipartFile(
                "foo",
                "foo.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "".getBytes())

        when:
        service.store(Paths.get('a/b/c/d/test'), file)

        then:
        thrown(StorageException)
    }

    def "store and load"() {
        given:
        def file = new MockMultipartFile(
                "foo",
                "foo.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "test content".getBytes())

        when:
        service.store(Paths.get('a/b/c/d/test'), file)
        def load = service.load(Paths.get('a/b/c/d/test'))

        then:
        load == root.resolve(Paths.get('a/b/c/d/test'))
    }

    def "load all files"() {
        given:
        def file = new MockMultipartFile(
                "foo",
                "foo.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "test content".getBytes())

        when:
        service.store(Paths.get('a/b/c/d/test1'), file)
        service.store(Paths.get('a/b/c/d/test2'), file)
        service.store(Paths.get('a/b/c/d/test3'), file)
        def all = service.loadAll()

        then:
        all.collect(Collectors.toSet()) == [
                Paths.get('a/b/c/d/test1'),
                Paths.get('a/b/c/d/test2'),
                Paths.get('a/b/c/d/test3')
        ] as Set
    }

    def "file can be loaded as resource"() {
        given:
        def file = new MockMultipartFile(
                "foo",
                "foo.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "test content".getBytes())

        when:
        service.store(Paths.get('a/b/c/d/test'), file)
        def resource = service.loadAsResource(Paths.get('a/b/c/d/test'))

        then:
        resource.exists()
        resource.isFile()
        resource.isReadable()
    }
}
