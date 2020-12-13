package cz.buben.gallery.security;

import cz.buben.gallery.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@SuppressWarnings("WeakerAccess")
@Service
@AllArgsConstructor
public class JwtProvider {

    private final KeyProvider keyProvider;

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(this.keyProvider.getPrivateKey())
                .compact();
    }

    public boolean validateToken(String jwt) {
        Jwts.parserBuilder()
                .setSigningKey(this.keyProvider.getPublicKey())
                .build()
                .parseClaimsJws(jwt);
        return true;
    }

    public String getUsernameFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(this.keyProvider.getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
