package InternshipProj.api.compile;

import InternshipProj.api.dto.CompileResponseDto;
import InternshipProj.api.events.EventCategory;
import InternshipProj.api.events.EventRadii;
import InternshipProj.api.events.EventsService;
import InternshipProj.api.ips.IPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CompileController {
    @Autowired
    private IPService ipService;

    @Autowired
    private EventsService eventsService;

    @GetMapping("/unified")
    public CompileResponseDto getUnifiedData(@RequestParam Integer userId,
                                             @RequestParam Integer numberOfEvents,
                                             @RequestParam EventRadii radius,
                                             @RequestParam EventCategory category) {
        // Get user location
        String location = String.valueOf(ipService.getIpLocation(userId));


        String weather = IPService.getWeatherForLocation(location);

        // Get events
        List<EventResponseDto> events = eventsService.getDatabase(userId, radius, category, numberOfEvents);

        // Build response
        return new UnifiedResponseDto(location, weather, events);
    }
}
}
