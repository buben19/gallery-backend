package cz.buben.gallery.mapper;

import cz.buben.gallery.dto.RoleDto;
import cz.buben.gallery.model.Privilege;
import cz.buben.gallery.model.Role;
import cz.buben.gallery.model.User;
import cz.buben.gallery.repository.PrivilegeRepository;
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

@SuppressWarnings("unused")
@Setter
@Mapper(componentModel = "spring")
public abstract class RoleMapper {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private UserRepository userRepository;

    @Nonnull
    @Mapping(target = "privileges", expression = "java(getPrivileges(role))")
    @Mapping(target = "users", expression = "java(getUsers(role))")
    public abstract RoleDto roleToDto(@Nonnull Role role);

    @Nonnull
    @InheritInverseConfiguration
    @Mapping(target = "privileges", expression = "java(findPrivileges(role.getPrivileges()))")
    @Mapping(target = "users", expression = "java(findUsers(role.getUsers()))")
    public abstract Role dtoToRole(@Nonnull RoleDto role);

    @Nonnull
    protected List<Long> getPrivileges(@Nonnull Role role) {
        return role.getPrivileges().stream().map(Privilege::getId).collect(Collectors.toList());
    }

    @Nonnull
    protected List<Long> getUsers(@Nonnull Role role) {
        return role.getUsers().stream().map(User::getId).collect(Collectors.toList());
    }

    @Nonnull
    protected Collection<Privilege> findPrivileges(@Nonnull List<Long> privilegesIds) {
        Collection<Privilege> privileges = new LinkedList<>();
        privilegesIds.forEach(privilegeId -> this.privilegeRepository.findById(privilegeId)
                .map(privileges::add)
                .orElseThrow(() -> new EntityNotFoundException("Can't find privilege with id: " + privilegeId)));
        return privileges;
    }

    @Nonnull
    protected Collection<User> findUsers(@Nonnull List<Long> userIds) {
        Collection<User> users = new LinkedList<>();
        userIds.forEach(userId -> this.userRepository.findById(userId)
                .map(users::add)
                .orElseThrow(() -> new EntityNotFoundException("Can't find user with id: " + userId)));
        return users;
    }
}
