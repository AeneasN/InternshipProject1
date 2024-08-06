package InternshipProj.api.events;

import InternshipProj.api.ips.IPTable;
import InternshipProj.api.users.Userid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventsRepository extends CrudRepository<EventsTable, Integer>{
    @Query("SELECT e FROM EventsTable e JOIN e.ipTable ip WHERE ip.city = :city")
    List<EventsTable> findByCity(@Param("city") String city, PageRequest pageRequest);

    @Query("SELECT e FROM EventsTable e JOIN e.ipTable ip WHERE ip.country = :country")
    List<EventsTable> findByCountry(@Param("country") String country, PageRequest pageRequest);

    @Query("SELECT e FROM EventsTable e JOIN e.ipTable ip WHERE ip.region = :region")
    List<EventsTable> findByRegion(@Param("region") String region, PageRequest pageRequest);

    boolean existsByEventCode(String eventCode);
    boolean existsByTitle(String title);
}
