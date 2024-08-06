package InternshipProj.api.events;

import InternshipProj.api.ips.IPTable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events")
public class EventsTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ip_id", nullable = false)
    private IPTable ipTable;

    private String eventCode;

    private String title;

    private LocalDateTime eventDate;

    private Boolean tag;

    private String category;

    public EventsTable(IPTable ipTable, String eventCode, String title, LocalDateTime eventDate, boolean tag, String category) {
        this.ipTable = ipTable;
        this.eventCode = eventCode;
        this.title = title;
        this.eventDate = eventDate;
        this.tag = tag;
        this.category = category;
    }
}
