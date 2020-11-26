package cz.buben.sre.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.hateoas.MediaTypes
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerSpecification extends Specification {

    @Autowired
    MockMvc mvc

    def "context loads"() {
        expect:
        mvc
    }

    def "get profile"() {
        when:
        def resultActions = this.mvc.perform(get('/api/profile'))

        then:
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
    }

    def "get image profile"() {
        when:
        def resultActions = this.mvc.perform(get('/api/profile/images'))

        then:
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.ALPS_JSON_VALUE))
    }

    def "get user profile"() {
        when:
        def resultActions = this.mvc.perform(get('/api/profile/users'))

        then:
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.ALPS_JSON_VALUE))
    }
}