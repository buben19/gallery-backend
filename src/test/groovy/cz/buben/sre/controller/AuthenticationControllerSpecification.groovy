package cz.buben.sre.controller

import cz.buben.sre.service.AuthenticationService
import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
@ContextConfiguration(classes = MockConfig)
class AuthenticationControllerSpecification extends Specification {

    @Autowired
    MockMvc mvc

    @Autowired
    AuthenticationService authenticationService

    def "context loads"() {
        expect:
        mvc
    }

    def "user can sigup"() {
        when:
        def resultActions = mvc.perform(post("/api/auth/signup")
                .content(JsonOutput.toJson([
                        login: 'login',
                        password: 'password',
                        email: 'user@example.com'
                ]))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ';charset=ISO-8859-1'))
                .andExpect(content().string('User registration successful'))

        then:
        resultActions.andDo(print())
                .andExpect(status().isOk())
    }
}
