package cz.buben.gallery.security

import cz.buben.gallery.model.User
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class JwtAuthenticationFilterSpecification extends Specification {

    JwtProvider jwtProvider = Mock()
    UserDetailsService userDetailsService = Mock()

    def filter = new JwtAuthenticationFilter(jwtProvider, userDetailsService)

    MockMvc mvc = MockMvcBuilders.standaloneSetup(new TestController())
            .addFilter(filter)
            .build()

    def setup() {
        SecurityContextHolder.clearContext()
    }

    def cleanup() {
        SecurityContextHolder.clearContext()
    }

    def "filter will authenticate user"() {
        expect:
        SecurityContextHolder.getContext().getAuthentication() == null

        when:
        def resultActions = this.mvc.perform(get('/test')
                .header('Authorization', 'Bearer token'))

        then:
        1 * jwtProvider.getUserLogin('token') >> 'user'
        1 * userDetailsService.loadUserByUsername('user') >> new User(
                id: 1,
                login: 'user',
                password: 'password',
                email: 'user@example.com',
                enabled: true
        )

        then:
        resultActions
                .andDo(print())
                .andExpect(status().isOk())

        then:
        SecurityContextHolder.getContext().getAuthentication().getPrincipal() == new User(
                id: 1,
                login: 'user',
                password: 'password',
                email: 'user@example.com',
                enabled: true
        )
    }

    def "filter throws exception when JWT expires"() {
        when:
        this.mvc.perform(get('/test')
                .header('Authorization', 'Bearer token'))

        then:
        1 * jwtProvider.getUserLogin('token') >> { throw new ExpiredJwtException(null, null, 'Test message') }

        then:
        thrown(ExpiredJwtException)
    }

    @RestController
    private class TestController {

        @GetMapping("/test")
        void test() {
        }
    }
}
