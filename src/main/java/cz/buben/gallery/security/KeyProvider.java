package cz.buben.gallery.security;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Key provides is responsible for obtaining keys from keystore.
 *
 * <p>Keystore can be specified at application start or created at runtime.</p>
 */
public interface KeyProvider {

    PrivateKey getPrivateKey() throws SecurityException;

    PublicKey getPublicKey() throws SecurityException;
}
