package cz.buben.gallery.security;

import cz.buben.gallery.model.RefreshToken;
import cz.buben.gallery.model.User;
import cz.buben.gallery.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;

@SuppressWarnings("WeakerAccess")
@Service
@AllArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final Supplier<UUID> uuidSupplier;
    private final Clock clock;
    private final Supplier<Duration> expirationSupplier;

    @Nonnull
    public RefreshToken create(@Nonnull Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        /* Delete all old refresh tokens for this user. */
        this.refreshTokenRepository.deleteByUser(user);

        Instant now = Instant.now(this.clock);
        return this.refreshTokenRepository.save(RefreshToken.builder()
                .token(this.uuidSupplier.get().toString())
                .created(now)
                .user(user)
                .build());
    }

    @Nonnull
    public RefreshToken update(@Nonnull String token) {
        RefreshToken refreshToken = this.refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Can't find token: " + token));
        if (refreshToken.isValid(this.expirationSupplier.get(), this.clock)) {
            User user = refreshToken.getUser();

            /* Delete old token. */
            this.refreshTokenRepository.delete(refreshToken);

            /* Create new token. */
            Instant now = Instant.now(this.clock);
            return this.refreshTokenRepository.save(RefreshToken.builder()
                    .token(this.uuidSupplier.get().toString())
                    .created(now)
                    .user(user)
                    .build());
        } else {
            this.refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Token expired");
        }
    }
}