package cz.buben.gallery.mapper;

import cz.buben.gallery.dto.UserDto;
import cz.buben.gallery.model.Role;
import cz.buben.gallery.model.User;
import cz.buben.gallery.repository.RoleRepository;
import cz.buben.gallery.repository.UserRepository;
import lombok.Setter;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"unused"})
@Setter
@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Nonnull
    @Mapping(target = "roles", expression = "java(getRoles(user))")
    public abstract UserDto userToDto(@Nonnull User user);

    @Nonnull
    @InheritInverseConfiguration
    @Mapping(target = "roles", expression = "java(findRoles(user.getRoles()))")
    @Mapping(target = "password", expression = "java(findPassword(user.getId()))")
    public abstract User dtoToUser(@Nonnull UserDto user);

    @Nonnull
    protected List<Long> getRoles(@Nonnull User user) {
        return user.getRoles().stream().map(Role::getId).collect(Collectors.toList());
    }

    @Nonnull
    protected Collection<Role> findRoles(@Nonnull List<Long> roles) {
        List<Role> roleList = new LinkedList<>();
        roles.forEach(roleId -> this.roleRepository.findById(roleId)
                .map(roleList::add)
                .orElseThrow(() -> new EntityNotFoundException("Can't find role with id: " + roleId)));
        return roleList;
    }

    @Nonnull
    protected String findPassword(@Nonnull Long userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find user with id: " + userId))
                .getPassword();
    }
}
