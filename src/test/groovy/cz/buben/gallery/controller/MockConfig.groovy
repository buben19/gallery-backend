package cz.buben.gallery.controller

import cz.buben.gallery.security.AuthenticationService
import cz.buben.gallery.security.JwtProvider
import cz.buben.gallery.service.ImageService
import cz.buben.gallery.service.PrivilegeService
import cz.buben.gallery.service.RoleService
import cz.buben.gallery.service.UserService
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
    UserService userService() {
        return detachedMockFactory.Mock(UserService)
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
    PrivilegeService privilegeService() {
        return detachedMockFactory.Mock(PrivilegeService)
    }

    @Bean
    RoleService roleService() {
        return detachedMockFactory.Mock(RoleService)
    }

    @Bean
    JwtProvider jwtProvider() {
        return detachedMockFactory.Mock(JwtProvider)
    }
}