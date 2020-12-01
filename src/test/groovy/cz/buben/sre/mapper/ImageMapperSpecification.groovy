package cz.buben.sre.mapper

import cz.buben.sre.dto.ImageDto
import cz.buben.sre.model.Image
import cz.buben.sre.model.User
import cz.buben.sre.repository.ImageRepository
import cz.buben.sre.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import javax.persistence.EntityNotFoundException
import java.nio.file.Paths
import java.time.Instant

@SpringBootTest
class ImageMapperSpecification extends Specification {

    @Autowired
    UserRepository userRepository

    @Autowired
    ImageRepository imageRepository

    @Autowired
    ImageMapper mapper


    def setup() {
        userRepository.deleteAll()
        imageRepository.deleteAll()
    }

    def "context loads"() {
        expect:
        userRepository
        imageRepository
        mapper
    }

    def "map image to DTO"() {
        when: "Create user."
        def user = userRepository.save(new User(
                login: 'user',
                password: 'password',
                email: 'user@example.com',
                created: Instant.EPOCH
        ))

        then:
        user && user.id

        when: "Create image."
        def image = this.imageRepository.save(new Image(
                title: 'image',
                path: Paths.get('img/img.jpg'),
                owner: user,
        ))

        then:
        image && image.id

        when:
        def dto = mapper.imageToDto(image)

        then:
        dto == new ImageDto(
                id: image.id,
                title: 'image',
                owner: user.id
        )
    }

    @Transactional
    def "map DTO to image"() {
        when: "Create user."
        def user = userRepository.save(new User(
                login: 'user',
                password: 'password',
                email: 'user@example.com',
                created: Instant.EPOCH
        ))

        then:
        user && user.id

        when: "Create image."
        def image = this.imageRepository.save(new Image(
                title: 'image',
                path: Paths.get('img/img.jpg'),
                owner: user,
        ))

        then:
        image && image.id

        when:
        def mapped = mapper.dtoToImage(new ImageDto(
                id: image.id,
                title: 'image',
                owner: user.id
        ))

        then:
        mapped == image
    }

    @Transactional
    def "unknown image throws exception"() {
        when:
        mapper.dtoToImage(new ImageDto(
                id: 1,
                title: 'image',
                owner: 1
        ))

        then:
        thrown(EntityNotFoundException)
    }

    @Transactional
    def "unknown owner throws exception"() {
        when: "Create user."
        def user = userRepository.save(new User(
                login: 'user',
                password: 'password',
                email: 'user@example.com',
                created: Instant.EPOCH
        ))

        then:
        user && user.id

        when: "Create image."
        def image = this.imageRepository.save(new Image(
                title: 'image',
                path: Paths.get('img/img.jpg'),
                owner: user,
        ))

        then:
        image && image.id

        when:
        mapper.dtoToImage(new ImageDto(
                id: image.id,
                title: 'image',
                owner: user.id + 1
        ))

        then:
        thrown(EntityNotFoundException)
    }
}
