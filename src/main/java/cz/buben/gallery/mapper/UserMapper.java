package cz.buben.gallery.mapper;

import cz.buben.gallery.dto.UserDto;
import cz.buben.gallery.model.Role;
import cz.buben.gallery.model.User;
import cz.buben.gallery.repository.RoleRepository;
import cz.buben.gallery.repository.UserRepository;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Nonnull
    @Mapping(target = "roles", expression = "java(getRoles(user))")
    @Mapping(target = "password", expression = "java(null)")
    public abstract UserDto userToDto(@Nonnull User user);

    @Nonnull
    @InheritInverseConfiguration
    @Mapping(target = "roles", expression = "java(findRoles(user.getRoles()))")
    @Mapping(target = "password", expression = "java(encodeOrFindPassword(user.getPassword(), user.getId()))")
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

    /**
     * Encode password into encoded form if password is present. Find existing password if given password is blank.
     *
     * @param password Password from DTO.
     * @param userId User id which this mapping is related to.
     *
     * @return Encoded password.
     */
    @Nonnull
    protected String encodeOrFindPassword(@Nullable String password, @Nonnull Long userId) {
        if (StringUtils.isBlank(password)) {
            return this.userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Can't find user with id: " + userId))
                    .getPassword();
        } else {
            return this.passwordEncoder.encode(password);
        }
    }
}
