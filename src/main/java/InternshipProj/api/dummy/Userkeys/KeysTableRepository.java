package InternshipProj.api.dummy.Userkeys;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface KeysTableRepository extends CrudRepository<KeysTable, Long> {
    Optional<KeysTable> findByKeyAndApi(String key, String api);

    List<KeysTable> findByUserId(Long userId);
}