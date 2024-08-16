package InternshipProj.api.dummy;

import InternshipProj.api.events.EventsController;
import InternshipProj.api.events.EventsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import InternshipProj.api.dto.EventRequestDto;
import InternshipProj.api.dto.EventResponseDto;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EventsController.class)
@ContextConfiguration(classes = {
        EventsController.class,
        EventsService.class,
        // Add other necessary configurations if required
})
public class EventsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventsService eventsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    public void getCurrentEvents() {
        // arrange
        EventResponseDto eventResponseDto = new EventResponseDto();
        eventResponseDto.setEventName("Sample Event");
        eventResponseDto.setDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        eventResponseDto.setCategory("Music");
        eventResponseDto.setType("user created");

        when(eventsService.getDatabase(any(), any(), any(), any(), any()))
                .thenReturn(Collections.singletonList(eventResponseDto));

        // act
        mockMvc.perform(get("/api/events/current")
                        .param("userId", "1")
                        .param("radius", "CITY")
                        .param("category", "Music")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].eventName").value("Sample Event"))
                .andExpect(jsonPath("$[0].date").value(eventResponseDto.getDate()))
                .andExpect(jsonPath("$[0].category").value("Music"))
                .andExpect(jsonPath("$[0].type").value("user created"));
    }

    @Test
    @SneakyThrows
    public void createEvent() {
        // arrange
        EventResponseDto eventResponseDto = new EventResponseDto();
        eventResponseDto.setEventName("Sample Event");
        eventResponseDto.setDate(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        eventResponseDto.setCategory("Music");
        eventResponseDto.setType("user created");

        when(eventsService.createUserEvent(any(Long.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn(eventResponseDto);

        EventRequestDto requestDto = new EventRequestDto();
        requestDto.setUserId(1);

        // act
        mockMvc.perform(post("/api/events/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName").value("Sample Event"))
                .andExpect(jsonPath("$.date").value(eventResponseDto.getDate()))
                .andExpect(jsonPath("$.category").value("Music"))
                .andExpect(jsonPath("$.type").value("user created"));
    }
}
