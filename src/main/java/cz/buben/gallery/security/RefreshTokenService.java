package cz.buben.gallery.security;

import cz.buben.gallery.model.RefreshToken;
import cz.buben.gallery.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import javax.persistence.EntityNotFoundException;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;

@Service
@Transactional
@AllArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final Supplier<UUID> uuidSupplier;
    private final Clock clock;

    @Nonnull
    public String generate() {
        RefreshToken token = this.refreshTokenRepository.save(RefreshToken.builder()
                .token(this.uuidSupplier.get().toString())
                .created(Instant.now(this.clock))
                .build());
        return token.getToken();
    }

    public void validate(@Nonnull String token) {
        this.refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("Can't find refresh token: " + token));
    }

    public void delete(@Nonnull String token) {
        this.refreshTokenRepository.deleteByToken(token);
    }
}
