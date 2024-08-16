package InternshipProj.api.dummy;

import InternshipProj.api.dto.EventResponseDto;
import InternshipProj.api.events.*;
import InternshipProj.api.ips.IPRepository;
import InternshipProj.api.ips.IPTable;
import InternshipProj.api.users.Userid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EventsServiceTests {

    @Mock
    private EventsRepository eventsRepository;

    @Mock
    private IPRepository ipRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EventsService eventsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testGetPlaceIdSuccess() {
//        // Arrange
//        String city = "New York";
//        String country = "US";
//        String expectedPlaceId = "1234";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + EventsService.API_KEY);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        Map<String, Object> responseBody = Map.of("results", List.of(Map.of("id", expectedPlaceId)));
//        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
//
//        when(restTemplate.exchange(
//                anyString(),
//                eq(HttpMethod.GET),
//                eq(entity),
//                eq(new ParameterizedTypeReference<Map<String, Object>>() {})))
//                .thenReturn(responseEntity);
//
//        // Act
//        String placeId = eventsService.getPlaceId(city, country);
//
//        // Assert
//        assertEquals(expectedPlaceId, placeId);
//    }

    @Test
    void testGetPlaceIdFailure() {
        // Arrange
        String city = "UnknownCity";
        String country = "UnknownCountry";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + EventsService.API_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                eq(entity),
                eq(new ParameterizedTypeReference<Map<String, Object>>() {})))
                .thenReturn(responseEntity);

        // Act
        String placeId = eventsService.getPlaceId(city, country);

        // Assert
        assertNull(placeId);
    }

//    @Test
//    void testGetEventsFromPlaceId() {
//        // Arrange
//        String placeId = "1234";
//        EventRadii radius = EventRadii.CITY;
//        EventCategory category = EventCategory.SPORTS;
//        LocalDate startDate = LocalDate.now();
//        LocalDate endDate = LocalDate.now().plusDays(1);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + EventsService.API_KEY);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        Map<String, Object> event = Map.of(
//                "id", "event1",
//                "title", "Event Title",
//                "start", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
//                "category", "sports"
//        );
//
//        List<Map<String, Object>> eventsList = List.of(event);
//        Map<String, Object> responseBody = Map.of("results", eventsList);
//        ResponseEntity<Map<String, Object>> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
//
//        when(restTemplate.exchange(
//                anyString(),
//                eq(HttpMethod.GET),
//                eq(entity),
//                eq(new ParameterizedTypeReference<Map<String, Object>>() {})))
//                .thenReturn(responseEntity);
//
//        // Act
//        List<Map<String, Object>> events = eventsService.getEventsFromPlaceId(placeId, radius, category, startDate, endDate);
//
//        // Assert
//        assertNotNull(events);
//        assertFalse(events.isEmpty());
//        assertEquals("event1", events.get(0).get("id"));
//    }




//    @Test
//    void testGetCurrentEvents() {
//        // Arrange
//        Long userId = 1L;
//        EventRadii radius = EventRadii.CITY;
//        EventCategory category = EventCategory.SPORTS;
//        LocalDate startDate = LocalDate.now();
//        LocalDate endDate = LocalDate.now().plusDays(1);
//
//        Userid user = new Userid();
//        user.setId(userId);
//
//        IPTable ipTable = new IPTable(user, "192.168.1.1");
//        ipTable.setCity("New York");
//        ipTable.setCountry("US");
//
//        when(ipRepository.findMostRecentByUser(any(), any())).thenReturn(List.of(ipTable));
//
//        String placeId = "1234";
//        when(eventsService.getPlaceId(ipTable.getCity(), ipTable.getCountry())).thenReturn(placeId);
//
//        Map<String, Object> event = Map.of(
//                "id", "event1",
//                "title", "Event Title",
//                "start", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
//                "category", "sports"
//        );
//
//        List<Map<String, Object>> eventsList = List.of(event);
//        when(eventsService.getEventsFromPlaceId(anyString(), any(), any(), any(), any())).thenReturn(eventsList);
//
//        when(eventsRepository.existsByEventCode(anyString())).thenReturn(false);
//
//        // Act
//        List<Map<String, Object>> events = eventsService.getCurrentEvents(user, radius, category, startDate, endDate);
//
//        // Assert
//        assertNotNull(events);
//        assertFalse(events.isEmpty());
//    }


//    @Test
//    void testGetDatabase() {
//        // Arrange
//        Long userId = 1L;
//        EventRadii radius = EventRadii.CITY;
//        EventCategory category = EventCategory.SPORTS;
//        LocalDate startDate = LocalDate.now();
//        LocalDate endDate = LocalDate.now().plusDays(1);
//
//        Userid user = new Userid();
//        user.setId(userId);
//
//        IPTable ipTable = new IPTable(user, "192.168.1.1");
//        ipTable.setCity("New York");
//        ipTable.setCountry("US");
//
//        when(ipRepository.findMostRecentByUser(any(), any())).thenReturn(List.of(ipTable));
//
//        String placeId = "1234";
//        when(eventsService.getPlaceId(ipTable.getCity(), ipTable.getCountry())).thenReturn(placeId);
//
//        Map<String, Object> event = Map.of(
//                "id", "event1",
//                "title", "Event Title",
//                "start", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
//                "category", "sports"
//        );
//
//        List<Map<String, Object>> eventsList = List.of(event);
//        when(eventsService.getEventsFromPlaceId(anyString(), any(), any(), any(), any())).thenReturn(eventsList);
//
//        EventsTable eventTable = new EventsTable(ipTable, "event1", "Event Title", LocalDateTime.now(), true, "sports");
//        when(eventsRepository.findByCity(anyString(), any())).thenReturn(List.of(eventTable));
//
//        // Act
//        List<EventResponseDto> eventResponseDtos = eventsService.getDatabase(user, radius, category, startDate, endDate);
//
//        // Assert
//        assertNotNull(eventResponseDtos);
//        assertFalse(eventResponseDtos.isEmpty());
//        assertEquals("Event Title", eventResponseDtos.get(0).getEventName());
//    }


    @Test
    void testGenerateRandomEventCode() {
        // Arrange
        EventsService eventsService = new EventsService();

        // Act
        String eventCode = eventsService.generateRandomEventCode();

        // Assert
        assertNotNull(eventCode);
        assertEquals(18, eventCode.length());
    }

    @Test
    void testCreateUserEventSuccess() {
        // Arrange
        Long userId = 1L;
        String title = "New Event";
        String eventDateStr = "08-16-2024:15:00";
        String category = "sports";

        Userid user = new Userid();
        user.setId(userId);

        IPTable ipTable = new IPTable(user, "192.168.1.1");
        ipTable.setCity("New York");
        ipTable.setCountry("US");

        when(ipRepository.findMostRecentByUser(any(), any())).thenReturn(List.of(ipTable));
        when(eventsRepository.existsByTitle(anyString())).thenReturn(false);

        EventsTable savedEvent = new EventsTable(ipTable, "generatedEventCode", title, LocalDateTime.now(), false, category);
        when(eventsRepository.save(any(EventsTable.class))).thenReturn(savedEvent);

        // Act
        EventResponseDto responseDto = eventsService.createUserEvent(userId, title, eventDateStr, category);

        // Assert
        assertNotNull(responseDto);
        assertEquals(title, responseDto.getEventName());
    }
}
