package InternshipProj.api.users;

import InternshipProj.api.user_keys.KeysTable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;

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

//Default Constructors
    public Userid(){}

    public Userid(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "Username must not be Null") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Username must not be Null") String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
