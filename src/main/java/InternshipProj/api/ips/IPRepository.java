package InternshipProj.api.ips;

import InternshipProj.api.users.Userid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


import java.util.List;


public interface IPRepository extends CrudRepository<IPTable, Integer> {
    @Query("SELECT ip FROM IPTable ip WHERE ip.user = :user ORDER BY ip.id DESC")
    List<IPTable> findMostRecentByUser(@Param("user") Userid user, Pageable pageable);
}
