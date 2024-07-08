package InternshipProj.api.user_keys;

import InternshipProj.api.users.Userid;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KeysTableRepository extends CrudRepository<KeysTable, Long> {
    Optional<KeysTable> findByKeyAndApi(String key, String api);
    List<KeysTable> findByUserId(Long userId);
    List<KeysTable> findByUserIdAndIsActive(@Param("userId") Long userId, @Param("isActive") Integer isActive);
    long countByUser(Userid user);

}