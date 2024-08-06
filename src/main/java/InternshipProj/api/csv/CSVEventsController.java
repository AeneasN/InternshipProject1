package InternshipProj.api.csv;

import InternshipProj.api.dto.EventResponseDto;
import InternshipProj.api.events.EventCategory;
import InternshipProj.api.events.EventRadii;
import InternshipProj.api.events.EventsService;
import InternshipProj.api.users.Userid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@RestController
@RequestMapping("/api/events/csv")
public class CSVEventsController {

    @Autowired
    private EventsService eventsService;

    @Autowired
    private CSVService csvService;

    @PostMapping("/up")
    public ResponseEntity<String> uploadCSV(@RequestParam("csv") MultipartFile file) {
        try {
            List<String> errors = csvService.validateCsv(file);
            if (!errors.isEmpty()) {
                return ResponseEntity.badRequest().body(String.join("\n", errors));
            }

            List<String[]> data = csvService.readDoc(file);
            for (String[] row : data) {
                Long userId = Long.valueOf(row[0]);
                String title = row[1];
                String eventDate = row[2];
                EventCategory category = EventCategory.valueOf(row[3].toUpperCase().replace(' ', '_'));

                eventsService.createUserEvent(userId, title, eventDate, category.name());
            }
            return ResponseEntity.ok("File processed successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to process file");
        }
    }

    @GetMapping("/down")
    public ResponseEntity<String> downloadCsv(
            @RequestParam Integer userId,
            @RequestParam EventRadii radius,
            @RequestParam EventCategory category) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plus(Period.ofWeeks(1));
        Userid user = new Userid();
        user.setId(Long.valueOf(userId));

        List<EventResponseDto> events = eventsService.getDatabase(user, radius, category, startDate, endDate);
        StringWriter writer = new StringWriter();
        try {
            csvService.writeDoc(events, writer);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to generate CSV");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=events.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(writer.toString());
    }
}
