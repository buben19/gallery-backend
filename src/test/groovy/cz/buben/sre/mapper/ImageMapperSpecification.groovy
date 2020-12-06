package cz.buben.sre.mapper

import cz.buben.sre.dto.ImageDto
import cz.buben.sre.model.Image
import cz.buben.sre.model.User
import cz.buben.sre.repository.ImageRepository
import cz.buben.sre.repository.UserRepository
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import javax.persistence.EntityNotFoundException
import java.nio.file.Paths
import java.time.Instant

@SpringBootTest
class ImageMapperSpecification extends Specification {

    @SpringBean
    UserRepository userRepository = Mock()

    @SpringBean
    ImageRepository imageRepository = Mock()

    @Autowired
    ImageMapper mapper

    def "context loads"() {
        expect:
        userRepository
        imageRepository
        mapper
    }

    def "map image to DTO"() {
        when:
        def dto = mapper.imageToDto(new Image(
                id: 1,
                title: 'Test title',
                owner: new User(
                        id: 1,
                        login: 'login',
                        password: 'password',
                        email: 'user@example.com',
                        created: Instant.EPOCH
                )
        ))

        then:
        dto == new ImageDto(
                id: 1,
                title: 'Test title',
                owner: 1
        )
    }

    def "map DTO to image"() {
        when:
        def mapped = mapper.dtoToImage(new ImageDto(
                id: 1,
                title: 'Test title',
                owner: 1
        ))

        then:
        1 * imageRepository.findById(1) >> Optional.of(new Image(
                id: 1,
                title: 'Test title',
                path: Paths.get('image.jpg'),
                owner: new User(
                        id: 1,
                        login: 'login',
                        password: 'password',
                        email: 'user@example.com',
                        created: Instant.EPOCH
                )))

        and:
        1 * userRepository.findById(1) >> Optional.of(new User(
                id: 1,
                login: 'login',
                password: 'password',
                email: 'user@example.com',
                created: Instant.EPOCH
        ))

        and:
        mapped == new Image(
                id: 1,
                title: 'Test title',
                path: Paths.get('image.jpg'),
                owner: new User(
                        id: 1,
                        login: 'login',
                        password: 'password',
                        email: 'user@example.com',
                        created: Instant.EPOCH
                ))
    }

    def "unknown image throws exception"() {
        when:
        mapper.dtoToImage(new ImageDto(
                id: 1,
                title: 'image',
                owner: 1
        ))

        then:
        1 * imageRepository.findById(1) >> {throw new EntityNotFoundException('Test exception')}

        then:
        thrown(EntityNotFoundException)
    }

    def "unknown owner throws exception"() {
        when:
        mapper.dtoToImage(new ImageDto(
                id: 1,
                title: 'image',
                owner: 1
        ))

        then:
         1 * imageRepository.findById(1) >> Optional.of(new Image(
                id: 1,
                title: 'Test title',
                path: Paths.get('image.jpg'),
                owner: new User(
                        id: 1,
                        login: 'login',
                        password: 'password',
                        email: 'user@example.com',
                        created: Instant.EPOCH
                )))

        1 * userRepository.findById(1) >> {throw new EntityNotFoundException('Test exception')}

        then:
        thrown(EntityNotFoundException)
    }
}
