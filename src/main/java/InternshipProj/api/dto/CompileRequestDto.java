package InternshipProj.api.dto;

import InternshipProj.api.events.EventCategory;
import InternshipProj.api.events.EventRadii;

public class CompileRequestDto {
    private Integer userId;
    private Integer numberOfEvents;
    private EventRadii radius;
    private EventCategory category;
}
