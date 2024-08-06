package InternshipProj.api.dto;

import InternshipProj.api.events.EventsTable;
import lombok.Data;

@Data
public class EventResponseDto {
    private String eventName;
    private String date;
    private String category;
    private String type;

    public EventResponseDto(EventsTable event) {
        this.eventName = event.getTitle();
        this.date = String.valueOf(event.getEventDate());
        this.category = event.getCategory();
        this.type = event.getTag() ? "external" : "user created";
    }

}
