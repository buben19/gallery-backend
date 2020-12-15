package cz.buben.gallery.service

import cz.buben.gallery.dto.ImageDto
import cz.buben.gallery.mapper.ImageMapper
import cz.buben.gallery.model.Image
import cz.buben.gallery.model.User
import cz.buben.gallery.repository.ImageRepository
import cz.buben.gallery.security.AuthenticationService
import cz.buben.gallery.storage.StoragePathGenerator
import cz.buben.gallery.storage.StorageService
import spock.lang.Specification

import java.nio.file.Paths
import java.time.Instant

class ImageServiceSpecification extends Specification {

    ImageRepository imageRepository = Mock()
    ImageMapper imageMapper = Mock()
    StorageService storageService = Mock()
    AuthenticationService authenticationService = Mock()
    StoragePathGenerator storagePathGenerator = Mock()
    ImageService imageService = new ImageService(imageRepository, imageMapper, storageService, authenticationService,
            storagePathGenerator)

    def "get all images" () {
        when:
        def all = imageService.getAll()

        then:
        1 * imageRepository.findAll() >> [
            new Image(
                    id: 1,
                    title: 'title1',
                    path: Paths.get('image1.jpg'),
                    owner: new User(
                            id: 1,
                            login: 'user1',
                            password: 'password1',
                            email: 'user1@example.com',
                            created: Instant.EPOCH
                    )
            ),
            new Image(
                    id: 2,
                    title: 'title2',
                    path: Paths.get('image2.jpg'),
                    owner: new User(
                            id: 2,
                            login: 'user2',
                            password: 'password2',
                            email: 'user2@example.com',
                            created: Instant.EPOCH
                    )
            )
        ]

        then:
        1 * imageMapper.imageToDto(new Image(
                id: 1,
                title: 'title1',
                path: Paths.get('image1.jpg'),
                owner: new User(
                        id: 1,
                        login: 'user1',
                        password: 'password1',
                        email: 'user1@example.com',
                        created: Instant.EPOCH
                )
        )) >> new ImageDto(
                id: 1,
                title: 'title1',
                owner: 1
        )

        1 * imageMapper.imageToDto(new Image(
                id: 2,
                title: 'title2',
                path: Paths.get('image2.jpg'),
                owner: new User(
                        id: 2,
                        login: 'user2',
                        password: 'password2',
                        email: 'user2@example.com',
                        created: Instant.EPOCH
                )
        )) >> new ImageDto(
                id: 2,
                title: 'title2',
                owner: 2
        )

        then:
        all == [new ImageDto(
                id: 1,
                title: 'title1',
                owner: 1
        ),
        new ImageDto(
                id: 2,
                title: 'title2',
                owner: 2
        )]
    }

    def "get single image"() {
        when:
        def get = imageService.get(1)

        then:
        1 * imageRepository.findById(1) >> Optional.of(new Image(
                id: 1,
                title: 'title1',
                path: Paths.get('image1.jpg'),
                owner: new User(
                        id: 1,
                        login: 'user1',
                        password: 'password1',
                        email: 'user1@example.com',
                        created: Instant.EPOCH
                )
        ))

        then:
        1 * imageMapper.imageToDto(new Image(
                id: 1,
                title: 'title1',
                path: Paths.get('image1.jpg'),
                owner: new User(
                        id: 1,
                        login: 'user1',
                        password: 'password1',
                        email: 'user1@example.com',
                        created: Instant.EPOCH
                )
        )) >> new ImageDto(
                id: 1,
                title: 'title1',
                owner: 1
        )

        then:
        get == new ImageDto(
                id: 1,
                title: 'title1',
                owner: 1
        )
    }
}
