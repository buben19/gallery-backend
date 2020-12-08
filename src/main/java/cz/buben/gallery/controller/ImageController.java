package cz.buben.gallery.controller;

import cz.buben.gallery.dto.ImageDto;
import cz.buben.gallery.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@SuppressWarnings("unused")
@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/images")
    public ResponseEntity<List<ImageDto>> getAll() {
        return ResponseEntity.ok(this.imageService.getAll());
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<ImageDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(this.imageService.get(id));
    }

    /**
     * @deprecated Images should not be created like this.
     */
    @Deprecated
    @PostMapping("/images")
    public ResponseEntity<Long> createImage(@RequestBody ImageDto image) {
        Long id = this.imageService.create(image);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    /**
     * Upload image file to the server.
     *
     * @param image Image payload.
     *
     * @return Server response.
     */
    @PostMapping("/images/upload")
    public ResponseEntity<ImageDto> uploadImage(@RequestBody MultipartFile image) {
        try {
            ImageDto imageDto = this.imageService.saveImage(image);
            return new ResponseEntity<>(imageDto, HttpStatus.CREATED);
        } catch (Throwable ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ImageDto());
        }
    }

    /**
     * Download image from server.
     *
     * @param id Image id.
     *
     * @return Image file.
     */
    @GetMapping("/images/download/{id}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long id) {
        try {
            Resource resource = this.imageService.loadImage(id);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource);
        } catch (Throwable ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
