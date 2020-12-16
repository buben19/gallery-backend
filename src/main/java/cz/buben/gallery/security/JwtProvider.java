package cz.buben.gallery.security;

import cz.buben.gallery.model.Privilege;
import cz.buben.gallery.model.Role;
import cz.buben.gallery.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@SuppressWarnings("WeakerAccess")
@Service
@AllArgsConstructor
public class JwtProvider {

    private final KeyProvider keyProvider;
    private final Clock clock;
    private final Supplier<Duration> tokenDurationSupplier;

    @Nonnull
    public String generateToken(@Nonnull Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Instant now = Instant.now(this.clock);
        Instant expiration = now.plus(this.tokenDurationSupplier.get());
        return Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .claim("name", user.getUsername())
                .claim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .claim("privileges", user.getPrivileges().stream().map(Privilege::getName).collect(Collectors.toList()))
                .signWith(this.keyProvider.getPrivateKey())
                .compact();
    }

    @Nonnull
    public String getUserId(@Nonnull String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(this.keyProvider.getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("name", String.class);
    }

    @Nonnull
    public String generateTokenWithUsername(@Nonnull String username) {
        Instant now = Instant.now(this.clock);
        Instant expiration = now.plus(this.tokenDurationSupplier.get());
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(this.keyProvider.getPrivateKey())
                .compact();
    }
}
