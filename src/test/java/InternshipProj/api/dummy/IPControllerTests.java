package InternshipProj.api.dummy;

import InternshipProj.api.ips.IPController;
import InternshipProj.api.ips.IPService;
import InternshipProj.api.ips.IPTable;
import com.fasterxml.jackson.databind.ObjectMapper;
import InternshipProj.api.dto.CompWeatherResponseDto;
import InternshipProj.api.users.UserIDRepository;
import InternshipProj.api.users.Userid;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(IPController.class)
@ContextConfiguration(classes = {
        IPController.class,
        IPService.class,
        UserIDRepository.class
})
public class IPControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPService ipService;

    @MockBean
    private UserIDRepository userIDRepository;

//    @Test
//    @SneakyThrows
//    public void testGetLocationSuccess() {
//        // Arrange
//        Long userId = 1L;
//        IPTable mockIpTable = new IPTable(new Userid(), "128.243.80.167");
//        mockIpTable.setCity("SampleCity");
//        mockIpTable.setCountry("SampleCountry");
//
//        when(userIDRepository.findById(userId)).thenReturn(Optional.of(new Userid()));
//        when(ipService.getIpLocation(any(Userid.class), any(String.class))).thenReturn(mockIpTable);
//
//        // Act
//        MvcResult result = mockMvc.perform(get("/location")
//                        .param("userId", userId.toString()))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // Assert
//        String responseBody = result.getResponse().getContentAsString();
//        assertTrue(responseBody.contains("SampleCity"));
//        assertTrue(responseBody.contains("SampleCountry"));
//    }
//
//    @Test
//    @SneakyThrows
//    public void testGetWeatherSuccess() {
//        // Arrange
//        Long userId = 1L;
//        CompWeatherResponseDto mockWeatherResponse = new CompWeatherResponseDto();
//        mockWeatherResponse.setTemperature(25.0);
//        mockWeatherResponse.setLocation("SampleCity, SampleCountry");
//        mockWeatherResponse.setTimestamp("2024-08-16T00:00:00Z");
//
//        when(userIDRepository.findById(userId)).thenReturn(Optional.of(new Userid()));
//        when(ipService.getWeatherForUser(any(Userid.class))).thenReturn(mockWeatherResponse);
//
//        // Act
//        MvcResult result = mockMvc.perform(get("/weather")
//                        .param("userId", userId.toString()))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // Assert
//        String responseBody = result.getResponse().getContentAsString();
//        assertTrue(responseBody.contains("\"temperature\":25.0"));
//        assertTrue(responseBody.contains("\"location\":\"SampleCity, SampleCountry\""));
//        assertTrue(responseBody.contains("\"timestamp\":\"2024-08-16T00:00:00Z\""));
//    }
}
