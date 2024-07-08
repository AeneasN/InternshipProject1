package InternshipProj.api.users;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserIDRepository extends CrudRepository<Userid, Long> {
    Optional<Userid> findByEmail(String email);
    @Modifying
    @Query("UPDATE Userid u SET u.uses = 0")
    void resetAllUses();
}
