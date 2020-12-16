package cz.buben.gallery.repository;

import cz.buben.gallery.model.Role;
import org.springframework.data.repository.CrudRepository;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {

    @Nonnull
    Optional<Role> findByName(@Nonnull String name);
}
