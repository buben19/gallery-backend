package cz.buben.sre;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.util.UUID;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@SpringBootApplication
public class SpringReactExampleApplication {

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
}
