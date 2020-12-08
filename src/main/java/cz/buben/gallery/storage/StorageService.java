package cz.buben.gallery.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(Path path, MultipartFile file);

    Stream<Path> loadAll();

    Path load(Path file);

    Resource loadAsResource(Path filename);

    void deleteAll();
}
