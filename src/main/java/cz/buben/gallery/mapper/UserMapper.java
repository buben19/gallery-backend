package cz.buben.gallery.mapper;

import cz.buben.gallery.dto.UserDto;
import cz.buben.gallery.model.Privilege;
import cz.buben.gallery.model.Role;
import cz.buben.gallery.model.User;
import cz.buben.gallery.repository.RoleRepository;
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

@SuppressWarnings("unused")
@Setter
@Mapper(componentModel = "spring")
public abstract class UserMapper {

    @Autowired
    private RoleRepository roleRepository;

    @Mapping(target = "roles", expression = "java(getListOfRoles(user))")
    @Mapping(target = "privileges", expression = "java(getListOfPrivileges(user))")
    public abstract UserDto userToDto(User user);

    @InheritInverseConfiguration
    @Mapping(target = "roles", expression = "java(findRoles(user.getRoles()))")
    public abstract User dtoToUser(UserDto user);

    @Nonnull
    protected List<String> getListOfRoles(@Nonnull User user) {
        return user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
    }

    @Nonnull
    protected List<String> getListOfPrivileges(@Nonnull User user) {
        return user.getPrivileges().stream().map(Privilege::getName).collect(Collectors.toList());
    }

    @Nonnull
    protected Collection<Role> findRoles(@Nonnull List<String> roles) {
        List<Role> roleList = new LinkedList<>();
        roles.forEach(roleName -> this.roleRepository.findByName(roleName)
                .map(roleList::add)
                .orElseThrow(() -> new EntityNotFoundException("Can't find role: " + roleName)));
        return roleList;
    }
}
