package InternshipProj.api.events;

import InternshipProj.api.dto.EventResponseDto;
import InternshipProj.api.users.Userid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @PostMapping(value = "/up", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadCSV(@Parameter(description = "CSV file to upload",
            content = @Content(mediaType = "multipart/form-data",
                    schema = @Schema(type = "string", format = "binary")))
                                                @RequestParam("csv") MultipartFile file) {
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
            return ResponseEntity.status(500).body("csv generation failed");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=events.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(writer.toString());
    }
}
