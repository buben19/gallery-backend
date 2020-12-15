package cz.buben.gallery.repository;

import cz.buben.gallery.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    @PreAuthorize("hasPermission(#user, 'WRITE')")
    @Nonnull
    @Override
    <S extends User> S save(@Nonnull @Param("user") S user);


    Optional<User> findByLogin(String login);
}
