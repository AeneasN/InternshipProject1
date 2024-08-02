package InternshipProj.api.dto;

import lombok.Data;

@Data
public class EventResponseDto {
    private String eventName;
    private String date;
    private String location;
    private String description;

    public void EventRequestDto(String eventName, String date, String location, String description) {
        this.eventName = eventName;
        this.date = date;
        this.location = location;
        this.description = description;
    }

}
