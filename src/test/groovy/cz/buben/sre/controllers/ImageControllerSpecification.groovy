package cz.buben.sre.controllers

import cz.buben.sre.model.Image
import cz.buben.sre.model.User
import cz.buben.sre.repository.ImageRepository
import cz.buben.sre.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print

@SpringBootTest
@AutoConfigureMockMvc
class ImageControllerSpecification extends Specification {

    @Autowired
    MockMvc mvc

    @Autowired
    ImageRepository imageRepository

    @Autowired
    UserRepository userRepository

    def setup() {
        this.imageRepository.deleteAll()
        this.userRepository.deleteAll()
    }

    def "context loads"() {
        expect:
        mvc
        imageRepository
        userRepository
    }

    def "get all images"() {
        when:
        def user = this.userRepository.save(new User(
                firstName: 'name',
                lastName: 'surename',
                login: 'test',
                password: 'pass'
        ))
        this.imageRepository.save(new Image(
                title: 'title 1',
                path: 'path 1',
                owner: user
        ))
        this.imageRepository.save(new Image(
                title: 'title 2',
                path: 'path 2',
                owner: user
        ))

        def resultActions = this.mvc.perform(get('/api/images'))
                .andDo(print())

        then:
        resultActions
    }
}
