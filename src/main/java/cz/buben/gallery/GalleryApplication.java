package cz.buben.gallery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.time.Duration;
import java.util.UUID;
import java.util.function.Supplier;

import static cz.buben.gallery.Qualifiers.JWT_DURATION_SUPPLIER;
import static cz.buben.gallery.Qualifiers.REFRESH_TOKEN_DURATION_SUPPLIER;

@SuppressWarnings("unused")
@SpringBootApplication
public class GalleryApplication {

    @Value("media")
    private String root;

    public static void main(String[] args) {
        SpringApplication.run(GalleryApplication.class, args);
    }

    @Bean
    public Supplier<UUID> uuidSupplier() {
        return UUID::randomUUID;
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public Path storageRoot() {
        return Paths.get(this.root);
    }

    @Bean(JWT_DURATION_SUPPLIER)
    public Supplier<Duration> jwtDuration() {
        return () -> Duration.ofMinutes(30);
    }

    @Bean(REFRESH_TOKEN_DURATION_SUPPLIER)
    public Supplier<Duration> refreshTokenDuration() {
        return () -> Duration.ofDays(14);
    }
}
