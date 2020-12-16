package cz.buben.gallery.security;

import cz.buben.gallery.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.util.Date;

@SuppressWarnings("WeakerAccess")
@Service
@AllArgsConstructor
public class JwtProvider {

    private final KeyProvider keyProvider;
    private final Clock clock;
    private final Duration tokenDuration = Duration.ofMinutes(30);

    @Nonnull
    public JwtResult generateToken(@Nonnull Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        Instant now = Instant.now(this.clock);
        Instant expiration = now.plus(this.tokenDuration);
        String token = Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(this.keyProvider.getPrivateKey())
                .compact();
        return new JwtResult(token, expiration);
    }

    public boolean validateToken(@Nonnull String jwt) {
        Jwts.parserBuilder()
                .setSigningKey(this.keyProvider.getPublicKey())
                .build()
                .parseClaimsJws(jwt);
        return true;
    }

    @Nonnull
    public String getUsernameFromJwt(@Nonnull String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(this.keyProvider.getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    @Nonnull
    public JwtResult generateTokenWithUsername(@Nonnull String username) {
        Instant now = Instant.now(this.clock);
        Instant expiration = now.plus(this.tokenDuration);
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(this.keyProvider.getPrivateKey())
                .compact();
        return new JwtResult(token, expiration);
    }

    @Data
    public static class JwtResult {

        private final String token;
        private final Instant expiration;
    }
}
