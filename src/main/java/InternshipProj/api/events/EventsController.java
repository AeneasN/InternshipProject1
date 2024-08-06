package InternshipProj.api.events;

import InternshipProj.api.dto.EventResponseDto;
import InternshipProj.api.users.Userid;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventsController {

    @Autowired
    private EventsService eventsService;

    @GetMapping("/current")
    public List<EventResponseDto> getCurrentEvents(
            @RequestParam @Valid Integer userId,
            @RequestParam EventRadii radius,
            @RequestParam EventCategory category) {

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plus(Period.ofWeeks(1));
        Userid user = new Userid();
        user.setId(Long.valueOf(userId));
        return eventsService.getDatabase(user, radius, category, startDate, endDate);
    }

    @PostMapping("/create")
    public EventResponseDto createEvent(
            @RequestParam Long userId,
            @RequestParam String title,
            @RequestParam String eventDate,
            @RequestParam EventCategory category) {
        EventResponseDto event = eventsService.createUserEvent(userId, title, eventDate, category.name());
        return event;
    }
}
