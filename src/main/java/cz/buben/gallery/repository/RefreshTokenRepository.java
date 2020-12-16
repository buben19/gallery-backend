package cz.buben.gallery.repository;

import cz.buben.gallery.model.RefreshToken;
import cz.buben.gallery.model.User;
import org.springframework.data.repository.CrudRepository;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    /**
     * Delete all tokens for given user.
     *
     * @param user User entity which tokens should be deleted for.
     */
    void deleteByUser(@Nonnull User user);

    @Nonnull
    Optional<RefreshToken> findByToken(@Nonnull String token);
}
