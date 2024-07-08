package InternshipProj.api.users;

import InternshipProj.api.user_keys.KeysTable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;
@Data
@Entity
@Table(name = "users")
public class Userid {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy="user")
    private Set<KeysTable> keys;

    @NotBlank(message = "Username must not be Null")
    private String name;

    @NotBlank(message = "Email must not be Null")
    @Email(message = "Email must be valid")
    private String email;

    private int requestLimit = 100;
    private int uses;

//Default Constructors
    public Userid(){}

    public Userid(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.uses = 0;
    }
}
