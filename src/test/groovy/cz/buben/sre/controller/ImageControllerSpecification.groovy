package cz.buben.sre.controller

import cz.buben.sre.dto.ImageDto
import cz.buben.sre.service.ImageService
import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest
@ContextConfiguration(classes = MockConfig)
class ImageControllerSpecification extends Specification {

    @Autowired
    MockMvc mvc

    @Autowired
    ImageService imageService

    def "context loads"() {
        expect:
        mvc
    }

    @WithMockUser(value = "spring")
    def "get all images"() {
        when:
        def resultActions = this.mvc.perform(get('/api/images'))

        then:
        imageService.getAll() >> [
                new ImageDto(
                        id: 1,
                        title: 'Test title',
                        owner: 1
                )
        ]

        and:
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(JsonOutput.toJson([
                        [
                            id: 1,
                            title: 'Test title',
                            path: null,
                            owner: 1
                        ]
                ])))
    }

    @WithMockUser(value = "spring")
    def "get single image"() {
        when:
        def resultActions = this.mvc.perform(get('/api/images/1'))

        then:
        imageService.get(1) >> new ImageDto(
                id: 1,
                title: 'Test title',
                owner: 1
        )

        and:
        resultActions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(JsonOutput.toJson([
                            id: 1,
                            title: 'Test title',
                            path: null,
                            owner: 1
                ])))
    }

    @WithMockUser(value = "spring")
    def "create image"() {
        when:
        def resultActions = this.mvc.perform(post("/api/images")
                .content('''{
                            "title": "Test title",
                            "owner": 1
                        }''')
                .contentType(MediaType.APPLICATION_JSON))

        then:
        imageService.create(new ImageDto(
                title: 'Test title',
                owner: 1
        )) >> 1

        and:
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string('1'))
    }
}
