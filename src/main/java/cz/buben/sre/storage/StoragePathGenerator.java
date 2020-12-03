package cz.buben.sre.storage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

@Component
@AllArgsConstructor
public class StoragePathGenerator {

    private final Supplier<UUID> uuidSupplier;

    /**
     * Create path for media file.
     *
     * @param original Original file as string.
     *
     * @return Generated path within archive root directory.
     */
    @Nonnull
    public Path getStoragePath(@Nonnull String original) {
        Path path = Paths.get(original);
        return this.getStoragePath(path);
    }

    /**
     * Create path for media file.
     *
     * @param original Original file.
     *
     * @return Generated path within archive root directory.
     */
    @Nonnull
    public Path getStoragePath(@Nonnull Path original) {
        Optional<String> extensionOptional = this.getFileExtension(original);
        StringBuilder builder = new StringBuilder();
        builder.append(this.uuidSupplier.get()
                .toString()
                .replaceAll("-", "/"));
        extensionOptional.ifPresent(s -> builder.append(".").append(s));
        return Paths.get(builder.toString());
    }

    private Optional<String> getFileExtension(Path path) {
        return this.getFileExtension(path.getFileName().toString());
    }

    private Optional<String> getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }
}
