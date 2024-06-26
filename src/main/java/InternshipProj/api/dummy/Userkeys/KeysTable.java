package InternshipProj.api.dummy.Userkeys;

import InternshipProj.api.dummy.Userid.Userid;
import jakarta.persistence.*;

@Entity
public class KeysTable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Userid user;

    @Column(nullable = false)
    private String api;
    //Default Const.
    public KeysTable(long l, String key1, long l1, String api1){
    }
    //Getters and Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Userid getUser() {
        return user;
    }

    public void setUser(Userid user) {
        this.user = user;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }
}