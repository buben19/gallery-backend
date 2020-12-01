package cz.buben.sre.controller

import cz.buben.sre.service.AuthenticationService
import cz.buben.sre.service.ImageService
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
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
}