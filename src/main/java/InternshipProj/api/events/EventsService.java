package InternshipProj.api.events;

import InternshipProj.api.dto.EventResponseDto;
import InternshipProj.api.ips.IPRepository;
import InternshipProj.api.ips.IPTable;
import InternshipProj.api.users.Userid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class EventsService {
    @Autowired
    private EventsRepository eventsRepository;

    @Autowired
    private IPRepository ipRepository;

    public static final String API_KEY = "JWH_Zx29TRb44CQxENrvOUKmiAdxjeTJI0BIBEE-";

    public String getPlaceId(String city, String country) {
        String url = "https://api.predicthq.com/v1/places?";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_KEY);
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("country", country)
                .queryParam("q", city.replace(" ", "+"))
                .toUriString();

        try {
            ResponseEntity<Map> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class);
            Map<String, Object> body = response.getBody();
            if (body != null && body.containsKey("results")) {
                List<Map<String, Object>> results = (List<Map<String, Object>>) body.get("results");
                if (!results.isEmpty()) {
                    return (String) results.get(0).get("id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();  // Log the exception
        }

        System.out.println("No Place ID found for " + city + ", " + country);
        return null;
    }


    public List<Map<String, Object>> getEventsFromPlaceId(String placeId, EventRadii radius, EventCategory category, LocalDate startDate, LocalDate endDate) {
        String url = "https://api.predicthq.com/v1/events/";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + API_KEY);
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String uri = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("place.scope", placeId)
                .queryParam("active.gte", startDate.format(DateTimeFormatter.ISO_DATE))
                .queryParam("active.lte", endDate.format(DateTimeFormatter.ISO_DATE))
                .queryParam("sort", "rank")
                .queryParam("category", category.name().toLowerCase().replace('_', '-'))
                .toUriString();

        Map<String, Object> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class).getBody();

        if (response != null && response.containsKey("results")) {
            return (List<Map<String, Object>>) response.get("results");
        }

        return null;
    }

    public List<Map<String, Object>> getCurrentEvents(Userid userId, EventRadii radius, EventCategory category, LocalDate startDate, LocalDate endDate) {
        List<IPTable> ipEntry = ipRepository.findMostRecentByUser(userId, PageRequest.of(0, 1));

        if (!ipEntry.isEmpty()) {
            IPTable ipTable = ipEntry.get(0);
            String placeId = getPlaceId(ipTable.getCity(), ipTable.getCountry());

            if (placeId == null) {
                throw new RuntimeException("Place ID not found for " + ipTable.getCity() + ", " + ipTable.getCountry());
            }

            List<Map<String, Object>> events = getEventsFromPlaceId(placeId, radius, category, startDate, endDate);
            if (events != null) {
                for (Map<String, Object> eventMap : events) {
                    String eventCode = (String) eventMap.get("id");
                    if (eventsRepository.existsByEventCode(eventCode)) {
                        continue;
                    }
                    String title = (String) eventMap.get("title");
                    String start = (String) eventMap.get("start");
                    String eventCategory = (String) eventMap.get("category");

                    LocalDateTime eventDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME);

                    EventsTable event = new EventsTable(ipTable, eventCode, title, eventDate, true, eventCategory);
                    eventsRepository.save(event);
                }
            }

            return events;
        } else {
            throw new RuntimeException("No IP entry found for user.");
        }
    }

    public List<EventResponseDto> getDatabase(Userid userId, EventRadii radius, EventCategory category, LocalDate startDate, LocalDate endDate) {

        getCurrentEvents(userId, radius, category, startDate, endDate);

        List<IPTable> ipEntry = ipRepository.findMostRecentByUser(userId, PageRequest.of(0, 1));
        if (!ipEntry.isEmpty()) {
            IPTable ipTable = ipEntry.get(0);
            PageRequest pageRequest = PageRequest.of(0, 10);
            List<EventsTable> events = switch (radius) {
                case CITY -> eventsRepository.findByCity(ipTable.getCity(), pageRequest);
                case COUNTRY -> eventsRepository.findByCountry(ipTable.getCountry(), pageRequest);
                case REGION -> eventsRepository.findByRegion(ipTable.getRegion(), pageRequest);
                default -> throw new IllegalArgumentException("Invalid radius: " + radius);
            };
            return events.stream().map(EventResponseDto::new).collect(Collectors.toList());
        } else {
            throw new RuntimeException("No IP entry found for user.");
        }
    }

    public String generateRandomEventCode() {
        int length = 18;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }

    public EventResponseDto createUserEvent(Long userId, String title, String eventDateStr, String category) {
        Userid user = new Userid();
        user.setId(userId);

        List<IPTable> ipEntry = ipRepository.findMostRecentByUser(user, PageRequest.of(0, 1));

        if (ipEntry.isEmpty()) {
            throw new RuntimeException("No IP entry found for user.");
        }

        IPTable ipTable = ipEntry.get(0);

        if (eventsRepository.existsByTitle(title)) {
            throw new RuntimeException("Event with title '" + title + "' already exists.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy:HH:mm");
        LocalDateTime eventDate = LocalDateTime.parse(eventDateStr, formatter);
        String eventCode = generateRandomEventCode();
        EventsTable event = new EventsTable(ipTable, eventCode, title, eventDate, false, category);
        EventsTable savedEvent = eventsRepository.save(event);
        return new EventResponseDto(savedEvent);
    }
}

