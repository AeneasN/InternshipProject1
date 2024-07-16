package InternshipProj.api.user_keys;

import InternshipProj.api.users.Userid;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
public class KeysTable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable = false, unique = true)
    private String key;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Userid user;

//    @Column(nullable = false)
    private String api;

    private Integer isActive;
//Necessary getters and setters for converting from int to bool
    public boolean getIsActive() {
        return isActive != null && isActive == 1;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive ? 1 : 0;
    }

    public KeysTable(long l, String key1, long l1, String api1) {
    }
}