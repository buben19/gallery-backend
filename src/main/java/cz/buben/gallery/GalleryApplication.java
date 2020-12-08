package cz.buben.gallery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.util.UUID;
import java.util.function.Supplier;

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
}
