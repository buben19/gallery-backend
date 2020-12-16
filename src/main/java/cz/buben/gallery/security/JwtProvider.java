package cz.buben.gallery.security;

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

@SuppressWarnings("WeakerAccess")
@Service
@AllArgsConstructor
public class JwtProvider {

    private final KeyProvider keyProvider;
    private final Clock clock;

    @Nonnull
    public String generateToken(@Nonnull Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(this.keyProvider.getPrivateKey())
                .compact();
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
    public String generateTokenWithUsername(@Nonnull String username) {
        Instant now = Instant.now(this.clock);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .signWith(this.keyProvider.getPrivateKey())
                .setExpiration(Date.from(now.plus(Duration.ofMinutes(30))))
                .compact();
    }
}
