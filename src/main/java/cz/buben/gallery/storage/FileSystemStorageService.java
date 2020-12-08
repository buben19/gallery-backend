package cz.buben.gallery.storage;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class FileSystemStorageService implements StorageService {

    private final Path root;

    @Override
    public void init() {
        try {
            Files.createDirectories(this.root);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public void store(Path path, MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path destinationFile = this.root.resolve(path).normalize().toAbsolutePath();
            if (!destinationFile.toAbsolutePath().toString().startsWith(this.root.toAbsolutePath().toString())) {
                // This is a security check
                throw new StorageException("Cannot store file outside current directory.");
            }
            Path parent = destinationFile.getParent();
            if (!Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file: " + e.getMessage(), e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root)
                    .filter(path -> Files.isRegularFile(path))
                    .map(this.root::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(Path file) {
        return this.root.resolve(file);
    }

    @Override
    public Resource loadAsResource(Path file) {
        try {
            Resource resource = new UrlResource(this.root.resolve(file).toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + file);
            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + file, e);
        }
    }

    @Override
    public void deleteAll() {
        try {
            FileSystemUtils.deleteRecursively(this.root);
        } catch (IOException ex) {
            throw new StorageException("Can't delete storage directory: " + ex.getMessage(), ex);
        }
    }
}
