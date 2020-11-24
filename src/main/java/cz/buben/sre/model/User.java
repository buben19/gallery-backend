package cz.buben.sre.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "images")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String firstName;

    private String lastName;

    private String login;

    private String password;
}
