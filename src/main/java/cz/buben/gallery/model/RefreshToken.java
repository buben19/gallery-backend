package cz.buben.gallery.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

/**
 * Refresh tokens for obtaining new JWT token.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(generator = "refresh_token_sequence")
    private Long id;

    @Column(nullable = false)
    private String token;

    /**
     * Timestamp when refresh token was created.
     */
    @Column(nullable = false)
    private Instant created;

    /**
     * Timestamp when token will expire. If value is <code>null</code>, system wide expire duration should be used.
     */
    private Instant expire;

    /**
     * User instance which this token is created for.
     */
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private User user;
}
