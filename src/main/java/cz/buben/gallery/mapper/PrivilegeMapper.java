package cz.buben.gallery.mapper;

import cz.buben.gallery.dto.PrivilegeDto;
import cz.buben.gallery.model.Privilege;
import cz.buben.gallery.model.Role;
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
public abstract class PrivilegeMapper {

    @Autowired
    private RoleRepository roleRepository;

    @Nonnull
    @Mapping(target = "roles", expression = "java(getRoles(privilege))")
    public abstract PrivilegeDto privilegeToDto(@Nonnull Privilege privilege);

    @Nonnull
    @InheritInverseConfiguration
    @Mapping(target = "roles", expression = "java(findRoles(privilege.getRoles()))")
    public abstract Privilege dtoToPrivilege(@Nonnull PrivilegeDto privilege);

    @Nonnull
    protected List<Long> getRoles(@Nonnull Privilege privilege) {
        return privilege.getRoles().stream().map(Role::getId).collect(Collectors.toList());
    }

    @Nonnull
    protected Collection<Role> findRoles(@Nonnull List<Long> roleIds) {
        Collection<Role> roles = new LinkedList<>();
        roleIds.forEach(roleId -> this.roleRepository.findById(roleId)
                .map(roles::add)
                .orElseThrow(() -> new EntityNotFoundException("Can't find role with id: " + roleId)));
        return roles;
    }
}
