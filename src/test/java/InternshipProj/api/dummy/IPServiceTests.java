package InternshipProj.api.dummy;

import InternshipProj.api.dto.CompWeatherResponseDto;
import InternshipProj.api.dto.WeatherResponseDto;
import InternshipProj.api.ips.IPRepository;
import InternshipProj.api.ips.IPService;
import InternshipProj.api.ips.IPTable;
import InternshipProj.api.users.Userid;
import io.ipinfo.api.IPinfo;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IPServiceTests {

    @Mock
    private IPRepository ipRepository;

    @Mock
    private IPinfo ipinfo;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private IPService ipService;

    private CacheManager cacheManager = new ConcurrentMapCacheManager("weatherCache");

    @Test
    public void testGetIpLocationSuccess() throws RateLimitedException {
        // Arrange
        Userid user = new Userid();
        String ipAddress = "192.168.1.1";

        // Create a mock IPResponse
        IPResponse ipResponse = mock(IPResponse.class);
        when(ipinfo.lookupIP(anyString())).thenReturn(ipResponse);
        when(ipResponse.getCity()).thenReturn("CityName");
        when(ipResponse.getCountryCode()).thenReturn("CountryCode");
        when(ipResponse.getRegion()).thenReturn("RegionName");

        // Simulate saving IPTable
        IPTable expectedIpTable = new IPTable(user, ipAddress);
        expectedIpTable.setCity("CityName");
        expectedIpTable.setCountry("CountryCode");
        expectedIpTable.setRegion("RegionName");
        when(ipRepository.save(any(IPTable.class))).thenReturn(expectedIpTable);

        // Act
        IPTable result = ipService.getIpLocation(user, ipAddress);

        // Debugging output
        System.out.println("Result City: " + result.getCity());
        System.out.println("Result Country: " + result.getCountry());
        System.out.println("Result Region: " + result.getRegion());

        // Assert
        assertEquals("CityName", result.getCity(), "City does not match");
        assertEquals("CountryCode", result.getCountry(), "Country does not match");
        assertEquals("RegionName", result.getRegion(), "Region does not match");

        // Verify interactions
        verify(ipinfo).lookupIP(anyString());
        verify(ipRepository).save(any(IPTable.class));
    }

    @Test
    public void testGetIpLocationRateLimitedException() throws RateLimitedException {
        // Arrange
        Userid user = new Userid();
        String ipAddress = "192.168.1.1";
        when(ipinfo.lookupIP(anyString())).thenThrow(new RateLimitedException());

        // Act
        IPTable result = ipService.getIpLocation(user, ipAddress);

        // Assert
        assertNull(result);
        verify(ipinfo).lookupIP(anyString());
        verify(ipRepository, never()).save(any(IPTable.class));
    }

    @Test
    public void testGetWeatherForUserSuccess() throws RateLimitedException {
        // Arrange
        Userid user = new Userid();
        IPTable ipTable = new IPTable(user, "8.8.8.8");
        ipTable.setCity("CityName");
        ipTable.setCountry("CountryCode");
        when(ipRepository.findMostRecentByUser(any(Userid.class), any(PageRequest.class)))
                .thenReturn(Collections.singletonList(ipTable));

        IPResponse ipResponse = mock(IPResponse.class);
        when(ipinfo.lookupIP(anyString())).thenReturn(ipResponse);
        when(ipResponse.getLatitude()).thenReturn("10.0");
        when(ipResponse.getLongitude()).thenReturn("20.0");
        when(ipResponse.getCity()).thenReturn("CityName");
        when(ipResponse.getCountryCode()).thenReturn("CountryCode");

        WeatherResponseDto weatherResponse = new WeatherResponseDto();
        WeatherResponseDto.DataPoint dataPoint = new WeatherResponseDto.DataPoint();
        WeatherResponseDto.DataPoint.Coordinate coordinate = new WeatherResponseDto.DataPoint.Coordinate();
        WeatherResponseDto.DataPoint.Coordinate.DateValue dateValue = new WeatherResponseDto.DataPoint.Coordinate.DateValue();
        dateValue.setDate("2024-08-16T00:00:00Z");
        dateValue.setValue(25.0);
        coordinate.setDates(Collections.singletonList(dateValue));
        dataPoint.setCoordinates(Collections.singletonList(coordinate));
        weatherResponse.setData(Collections.singletonList(dataPoint));

        when(restTemplate.getForObject(anyString(), eq(WeatherResponseDto.class)))
                .thenReturn(weatherResponse);

        // Act
        CompWeatherResponseDto result = ipService.getWeatherForUser(user);

        // Assert
        assertEquals("CityName, CountryCode", result.getLocation());
        assertEquals(25.0, result.getTemperature());
        assertEquals("2024-08-16T00:00:00Z", result.getTimestamp());
        verify(ipinfo).lookupIP(anyString());
        verify(restTemplate).getForObject(anyString(), eq(WeatherResponseDto.class));
    }

    @Test
    public void testGetWeatherForUserNoIpTables() throws RateLimitedException {
        // Arrange
        Userid user = new Userid();
        when(ipRepository.findMostRecentByUser(any(Userid.class), any(PageRequest.class)))
                .thenReturn(Collections.emptyList());

        // Act
        CompWeatherResponseDto result = ipService.getWeatherForUser(user);

        // Assert
        assertNull(result);
        verify(ipinfo, never()).lookupIP(anyString());
        verify(restTemplate, never()).getForObject(anyString(), eq(WeatherResponseDto.class));
    }

    @Test
    public void testGetWeatherForUserRateLimitedException() throws RateLimitedException {
        // Arrange
        Userid user = new Userid();
        IPTable ipTable = new IPTable(user, "8.8.8.8");
        ipTable.setCity("CityName");
        ipTable.setCountry("CountryCode");
        when(ipRepository.findMostRecentByUser(any(Userid.class), any(PageRequest.class)))
                .thenReturn(Collections.singletonList(ipTable));

        when(ipinfo.lookupIP(anyString())).thenThrow(new RateLimitedException());

        // Act
        CompWeatherResponseDto result = ipService.getWeatherForUser(user);

        // Assert
        assertNull(result);
        verify(ipinfo).lookupIP(anyString());
        verify(restTemplate, never()).getForObject(anyString(), eq(WeatherResponseDto.class));
    }
}
