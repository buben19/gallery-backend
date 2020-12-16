package cz.buben.gallery.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

/**
 * Verification token is used when new user makes registration to the application. Its account is not activated yet
 * and user is supposed to perform activation request to server.
 *
 * <p>Verification token is usually send by email, which is provided during registration phase.</p>
 */
@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "verification_tokens")
public class VerificationToken {

    @Id
    @GeneratedValue(generator = "verification_token_sequence")
    private Long id;

    /**
     * Verification token string. Usually, this should by UUID.
     */
    @NotBlank(message = "Token is required")
    @Column(nullable = false)
    private String token;

    /**
     * User which this token belongs to.
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    // TODO: This filed should hold created timestamp. Expiration should be subject of configuration.
    @Column(nullable = false)
    private Instant expire;
}
