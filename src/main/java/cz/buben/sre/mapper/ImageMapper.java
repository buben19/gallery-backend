package cz.buben.sre.mapper;

import cz.buben.sre.dto.ImageDto;
import cz.buben.sre.model.Image;
import cz.buben.sre.model.User;
import cz.buben.sre.repository.UserRepository;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings({"WeakerAccess", "unused"})
@Mapper(componentModel = "spring")
public abstract class ImageMapper {

    @Autowired
    private UserRepository userRepository;

    @Mapping(target = "owner", expression = "java(image.getOwner().getId())")
    public abstract ImageDto imageToDto(Image image);

    @InheritInverseConfiguration
    @Mapping(target = "owner", expression = "java(findUser(image.getOwner()))")
    public abstract Image dtoToImage(ImageDto image);

    protected User findUser(long userId) {
        return this.userRepository.findById(userId).orElse(null);
    }
}
