package cz.buben.gallery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String authenticationToken;
    private String refreshToken;
    private Instant expiresAt;
    private String username;

    /**
     * Collection of roles assigned to user. Not sure if needed.
     */
    private Collection<String> roles;

    /**
     * Collection of privileges provided to user. Used to show/hide elements in user interface.
     */
    private Collection<String> privileges;
}
