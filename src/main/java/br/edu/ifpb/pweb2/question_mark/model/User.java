package br.edu.ifpb.pweb2.question_mark.model;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String username;
    private String password;
    private Boolean enabled;

    @OneToMany(mappedBy = "username")
    @ToString.Exclude
    List<Authority> authorities;
}
