package cz.buben.gallery.controller

import cz.buben.gallery.dto.AuthenticationResponse
import cz.buben.gallery.dto.LoginRequest
import cz.buben.gallery.dto.RegistrationRequest
import cz.buben.gallery.security.AuthenticationService
import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Ignore
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

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

        then:
        1 * authenticationService.signup(new RegistrationRequest(
                login: 'login',
                password: 'password',
                email: 'user@example.com'
        ))

        and:
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ';charset=ISO-8859-1'))
                .andExpect(content().string('User registration successful'))
    }

    @Ignore
    def "user signup can fail"() {
        when:
        def resultActions = mvc.perform(post("/api/auth/signup")
                .content(JsonOutput.toJson([
                        login: 'login',
                        password: 'password',
                        email: 'user@example.com'
                ]))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        1 * authenticationService.signup(new RegistrationRequest(
                login: 'login',
                password: 'password',
                email: 'user@example.com'
        )) >> {throw new RuntimeException("Authentication failed")}

        and:
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ';charset=ISO-8859-1'))
                .andExpect(content().string('User registration failed'))
    }

    def "user can be verified"() {
        given:
        def token = UUID.randomUUID().toString()

        when:
        def resultActions = this.mvc.perform(get('/api/auth/verify/' + token))

        then:
        1 * authenticationService.verifyAccount(token)

        and:
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ';charset=ISO-8859-1'))
                .andExpect(content().string('User successfully verified'))
    }

    @Ignore
    def "user verification can fail"() {
        given:
        def token = UUID.randomUUID().toString()

        when:
        def resultActions = this.mvc.perform(get('/api/auth/verify/' + token))

        then:
        1 * authenticationService.verifyAccount(token) >> {throw new RuntimeException("Authentication failed")}

        and:
        resultActions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE + ';charset=ISO-8859-1'))
                .andExpect(content().string('User verification failed'))
    }

    def "user can login"() {
        when:
        def resultActions = mvc.perform(post("/api/auth/login")
                .content(JsonOutput.toJson([
                        username: 'login',
                        password: 'password',
                ]))
                .contentType(MediaType.APPLICATION_JSON))

        then:
        1 * authenticationService.login(new LoginRequest(
                username: 'login',
                password: 'password'
        )) >> new AuthenticationResponse(
                jwt: 'token'
        )

        and:
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(JsonOutput.toJson([
                        jwt: 'token'
                ])))
    }
}
