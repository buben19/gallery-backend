package cz.buben.gallery.security;

import lombok.AccessLevel;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

@Setter
@Service
public class BasicKeyProvider implements KeyProvider {

    @Value("jks")
    private String keyStoreType;

    @Value("${gallery.keyStoreFile:keystore.jks}")
    private Resource keyStoreFile;

    @Value("secret")
    private String keyStorePassword;

    @Value("secret")
    private String keyPassword;

    @Value("entry")
    private String entryName;

    @Setter(AccessLevel.NONE)
    private KeyStore keyStore;

    @PostConstruct
    public void init() {
        assert StringUtils.isNotBlank(this.keyStoreType) : "Key store type must be set.";
        assert this.keyStoreFile.exists() : "Key store file doesn't exists.";
        assert StringUtils.isNotBlank(this.entryName) : "Entry name must be set.";
        assert this.keyPassword != null : "Key password can't be null";
        try {
            this.keyStore = KeyStore.getInstance(this.keyStoreType);
            InputStream stream = this.keyStoreFile.getInputStream();
            this.keyStore.load(stream, this.keyStorePassword == null ? null : this.keyPassword.toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException ex) {
            throw new SecurityException("Exception occurred while loading keystore", ex);
        }
    }

    @Override
    public PrivateKey getPrivateKey() throws SecurityException {
        try {
            return (PrivateKey) this.keyStore.getKey(this.entryName, this.keyPassword.toCharArray());
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException ex) {
            throw new SecurityException("Can't get private key", ex);
        }
    }

    @Override
    public PublicKey getPublicKey() throws SecurityException {
        try {
            return this.keyStore.getCertificate(this.entryName).getPublicKey();
        } catch (KeyStoreException ex) {
            throw new SecurityException("Exception occurred while retrieving public key from keystore", ex);
        }
    }
}
