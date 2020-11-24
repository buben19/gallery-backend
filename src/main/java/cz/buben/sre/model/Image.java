package cz.buben.sre.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class Image {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String path;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User owner;
}
