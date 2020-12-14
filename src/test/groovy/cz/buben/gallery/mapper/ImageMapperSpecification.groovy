package cz.buben.gallery.mapper

import cz.buben.gallery.dto.ImageDto
import cz.buben.gallery.model.Image
import cz.buben.gallery.model.User
import cz.buben.gallery.repository.ImageRepository
import cz.buben.gallery.repository.UserRepository
import org.mapstruct.factory.Mappers
import spock.lang.Specification

import javax.persistence.EntityNotFoundException
import java.nio.file.Paths
import java.time.Instant

class ImageMapperSpecification extends Specification {

    UserRepository userRepository = Mock()
    ImageRepository imageRepository = Mock()
    ImageMapper mapper

    def setup() {
        mapper = Mappers.getMapper(ImageMapper.class)
        mapper.setUserRepository(userRepository)
        mapper.setImageRepository(imageRepository)
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
