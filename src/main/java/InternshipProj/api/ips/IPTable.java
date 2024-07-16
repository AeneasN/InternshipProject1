package InternshipProj.api.ips;


import InternshipProj.api.users.Userid;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ips")
public class IPTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Userid user;

    private String ip;

    private String city;

    private String country;

    private String region;

    public IPTable(Userid user, String ip){
        this.user = user;
        this.ip = ip;
    }
}
