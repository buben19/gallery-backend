package cz.buben.sre.controller;

import cz.buben.sre.dto.ImageDto;
import cz.buben.sre.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/images")
    public ResponseEntity<Long> createImage(@RequestBody ImageDto image) {
        Long id = this.imageService.create(image);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }
}
