package cz.buben.sre.service;

import cz.buben.sre.dto.ImageDto;
import cz.buben.sre.mapper.ImageMapper;
import cz.buben.sre.model.Image;
import cz.buben.sre.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

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
}

