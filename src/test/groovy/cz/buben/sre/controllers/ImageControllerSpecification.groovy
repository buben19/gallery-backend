package cz.buben.sre.controllers

import cz.buben.sre.model.Image
import cz.buben.sre.repository.ImageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class ImageControllerSpecification extends Specification {

    @Autowired
    MockMvc mvc

    @Autowired
    ImageRepository repository

    def setup() {
        this.repository.deleteAll()
    }

    def "context loads"() {
        expect:
        mvc
        repository
    }

    def "get all images"() {
        when:
        this.repository.save(new Image(
                id: 1,
                title: 'title 1',
                path: 'path 1'
        ))
        this.repository.save(new Image(
                id: 2,
                title: 'title 2',
                path: 'path 2'
        ))

        def resultActions = this.mvc.perform(get('/api/images'))
                .andDo(print())

        then:
        resultActions
    }
}
