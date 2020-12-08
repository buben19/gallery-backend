package cz.buben.gallery.mapper;

import cz.buben.gallery.dto.ImageDto;
import cz.buben.gallery.model.Image;
import cz.buben.gallery.model.User;
import cz.buben.gallery.repository.ImageRepository;
import cz.buben.gallery.repository.UserRepository;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.nio.file.Path;

@SuppressWarnings({"WeakerAccess", "unused"})
@Mapper(componentModel = "spring")
public abstract class ImageMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Mapping(target = "owner", expression = "java(image.getOwner().getId())")
    public abstract ImageDto imageToDto(Image image);

    @InheritInverseConfiguration
    @Mapping(target = "path", expression = "java(findImagePath(image.getId()))")
    @Mapping(target = "owner", expression = "java(findUser(image.getOwner()))")
    public abstract Image dtoToImage(ImageDto image);

    protected User findUser(long userId) {
        return this.userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't fid user with id: " + userId));
    }

    protected Path findImagePath(long imageId) {
        return this.imageRepository.findById(imageId).orElseThrow
                (() -> new EntityNotFoundException("Can't find image with id: " + imageId))
                .getPath();
    }
}
