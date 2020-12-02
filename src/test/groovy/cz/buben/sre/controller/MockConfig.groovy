package cz.buben.sre.controller

import cz.buben.sre.security.JwtProvider
import cz.buben.sre.service.AuthenticationService
import cz.buben.sre.service.ImageService
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.core.userdetails.UserDetailsService
import spock.mock.DetachedMockFactory

@TestConfiguration
class MockConfig {

    def detachedMockFactory = new DetachedMockFactory()

    @Bean
    ImageService imageService() {
        return detachedMockFactory.Mock(ImageService)
    }

    @Bean
    AuthenticationService authenticationService() {
        return detachedMockFactory.Mock(AuthenticationService)
    }

    @Bean
    UserDetailsService userDetailsService() {
        return detachedMockFactory.Mock(UserDetailsService)
    }

    @Bean
    JwtProvider jwtProvider() {
        return detachedMockFactory.Mock(JwtProvider)
    }
}