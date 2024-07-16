package InternshipProj.api.users;

import InternshipProj.api.ips.IPTable;
import InternshipProj.api.user_keys.KeysTable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class Userid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy="user", cascade = CascadeType.REMOVE)
    private Set<KeysTable> keys;

    @OneToMany(mappedBy="user", cascade = CascadeType.REMOVE)
    private Set<IPTable> ips;

    @NotBlank(message = "Username must not be Null")
    private String name;

    @NotBlank(message = "Email must not be Null")
    @Email(message = "Email must be valid")
    private String email;

    private int requestLimit = 100;
    private int uses = 0;
    private String code;
    private int verified = 0;
//Default Constructor

    public Userid(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
