package cz.buben.sre;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Clock;
import java.util.UUID;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@SpringBootApplication
public class SpringReactExampleApplication {

    @Value("media")
    private String root;

    public static void main(String[] args) {
        SpringApplication.run(SpringReactExampleApplication.class, args);
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
