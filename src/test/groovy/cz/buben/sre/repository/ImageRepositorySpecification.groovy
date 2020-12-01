package cz.buben.sre.repository

import cz.buben.sre.model.Image
import cz.buben.sre.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.CrudRepository
import spock.lang.Specification

import java.nio.file.Paths
import java.time.Instant

@DataJpaTest
class ImageRepositorySpecification extends Specification {

    @Autowired
    UserRepository userRepository

    @Autowired
    ImageRepository imageRepository

    def setup() {
        userRepository.deleteAll()
        imageRepository.deleteAll()
    }

    def "context loads"() {
        expect:
        userRepository
        imageRepository
    }

    def "implements CrudRepository"() {
        expect:
        imageRepository instanceof CrudRepository
    }

    def "images can be found by their owner"() {
        when: "Create users."
        def user1 = this.userRepository.save(new User(
                login: 'user1',
                password: 'password1',
                email: 'user1@example.com',
                created: Instant.EPOCH
        ))

        def user2 = this.userRepository.save(new User(
                login: 'user2',
                password: 'password2',
                email: 'user2@example.com',
                created: Instant.EPOCH
        ))

        then:
        user1
        user2
        user1 != user2

        when: "Create images."
        def image1 = this.imageRepository.save(new Image(
                title: 'image1',
                path: Paths.get('img/img1.jpg'),
                owner: user1,
        ))
        def image2 = this.imageRepository.save(new Image(
                title: 'image2',
                path: Paths.get('img/img2.jpg'),
                owner: user1,
        ))
        def image3 = this.imageRepository.save(new Image(
                title: 'image3',
                path: Paths.get('img/img3.jpg'),
                owner: user2,
        ))
        def image4 = this.imageRepository.save(new Image(
                title: 'image4',
                path: Paths.get('img/img4.jpg'),
                owner: user2,
        ))
        def image5 = this.imageRepository.save(new Image(
                title: 'image5',
                path: Paths.get('img/img5.jpg'),
                owner: user2,
        ))

        then:
        image1 && image1.owner == user1
        image2 && image2.owner == user1
        image3 && image3.owner == user2
        image4 && image4.owner == user2
        image5 && image5.owner == user2

        when: "Find images for user1."
        def user1Images = imageRepository.findByOwner(user1)

        then:
        user1Images == [image1, image2]

        when: "Find images for user2"
        def user2Images = imageRepository.findByOwner(user2)

        then:
        user2Images == [image3, image4, image5]
    }
}
