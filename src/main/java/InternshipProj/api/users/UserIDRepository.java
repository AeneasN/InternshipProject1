package InternshipProj.api.users;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserIDRepository extends CrudRepository<Userid, Long> {
    Optional<Userid> findByEmail(String email);
    @Modifying
    @Transactional
    @Query("UPDATE Userid u SET u.uses = 0")
    void resetAllUses();
    Optional<Userid> findByCode(String code);
}
