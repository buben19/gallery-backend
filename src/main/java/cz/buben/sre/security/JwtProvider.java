package cz.buben.sre.security;

import cz.buben.sre.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

@Service
public class JwtProvider {

    private KeyStore keyStore;

    @PostConstruct
    public void init() {
        try {
            this.keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = this.getClass().getResourceAsStream("/test.jks");
            this.keyStore.load(resourceAsStream, "secret".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException ex) {
            throw new RuntimeException("Exception occurred while loading keystore", ex);
        }
    }

    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getLogin())
                .signWith(this.getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) this.keyStore.getKey("test", "secret".toCharArray());
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException ex) {
            throw new RuntimeException("Can't get private key", ex);
        }
    }

    private PublicKey getPublicKey() {
        try {
            return this.keyStore.getCertificate("springblog").getPublicKey();
        } catch (KeyStoreException ex) {
            throw new RuntimeException("Exception occurred while retrieving public key from keystore", ex);
        }
    }

    public boolean validateToken(String jwt) {
        Jwts.parserBuilder()
                .setSigningKey(this.getPublicKey())
                .build()
                .parseClaimsJws(jwt);
        return true;
    }

    public String getUsernameFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(this.getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
