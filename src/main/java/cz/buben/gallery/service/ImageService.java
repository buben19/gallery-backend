package cz.buben.gallery.service;

import cz.buben.gallery.dto.ImageDto;
import cz.buben.gallery.mapper.ImageMapper;
import cz.buben.gallery.model.Image;
import cz.buben.gallery.model.User;
import cz.buben.gallery.repository.ImageRepository;
import cz.buben.gallery.storage.StoragePathGenerator;
import cz.buben.gallery.storage.StorageService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final StorageService storageService;
    private final AuthenticationService authenticationService;
    private final StoragePathGenerator storagePathGenerator;

    public List<ImageDto> getAll() {
        return StreamSupport.stream(this.imageRepository.findAll().spliterator(), false)
                .map(this.imageMapper::imageToDto)
                .collect(Collectors.toList());
    }

    public ImageDto get(Long id) {
        return this.imageRepository.findById(id)
                .map(this.imageMapper::imageToDto)
                .orElse(null);
    }

    public Long create(ImageDto imageDto) {
        Image image = this.imageMapper.dtoToImage(imageDto);
        return this.imageRepository.save(image).getId();
    }

    public ImageDto saveImage(MultipartFile image) {
        String originalFilename = image.getOriginalFilename();
        if (StringUtils.isBlank(originalFilename)) {
            originalFilename = "untitled";
        }
        Path storagePath = this.storagePathGenerator.getStoragePath(originalFilename);
        this.storageService.store(storagePath, image);

        User user = this.authenticationService.getCurrentUser();
        Image save = this.imageRepository.save(Image.builder()
                .title(originalFilename)
                .path(storagePath)
                .owner(user)
                .build());
        return this.imageMapper.imageToDto(save);
    }

    public Resource loadImage(Long id) {
        Optional<Image> imageOptional = this.imageRepository.findById(id);
        Image image = imageOptional.orElseThrow(() -> new RuntimeException("Can't find image wth id: " + id));
        return this.storageService.loadAsResource(image.getPath());
    }
}

