package cz.buben.gallery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String login;
    private String email;
    private Instant created;
    private boolean enabled;
    private List<String> roles;
    private List<String> privileges;
}
