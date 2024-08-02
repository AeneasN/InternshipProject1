package InternshipProj.api.ips;

import InternshipProj.api.dto.CompWeatherResponseDto;
import InternshipProj.api.dto.WeatherResponseDto;
import InternshipProj.api.users.Userid;
import io.ipinfo.api.IPinfo;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class IPService {
    @Autowired
    private IPRepository ipRepository;
    @Autowired
    private IPinfo ipinfo;
    @Autowired
    private RestTemplate restTemplate;

    private static final String METEOMATICS_API_URL = "https://api.meteomatics.com/{datetime}/{parameters}/{latitude},{longitude}/json";
    private static final String METEOMATICS_USERNAME = "stratify_nicholas_aeneas";
    private static final String METEOMATICS_PASSWORD = "0DusI8zD1i";

    public IPTable getIpLocation(Userid user, String ipAddress) {
        IPResponse response;
        try {
            response = ipinfo.lookupIP(ipAddress);
        } catch (RateLimitedException e) {
            return null;
        }
        String city = response.getCity();
        String country = response.getCountryCode();
        String region = response.getRegion();

        IPTable ipTable = new IPTable(user, ipAddress);
        ipTable.setCity(city);
        ipTable.setCountry(country);
        ipTable.setRegion(region);

        return ipRepository.save(ipTable);
    }

    @Cacheable(value = "weatherCache", key = "#user.id")
    public CompWeatherResponseDto getWeatherForUser(Userid user) {
        if (user == null) {
            return null;
        }

        List<IPTable> ipTables = ipRepository.findMostRecentByUser(user, PageRequest.of(0, 1));
        if (ipTables.isEmpty()) {
            return null;
        }

        //IPTable ipTable = ipTables.get(0);
        //String ipAddress = ipTable.getIp();
        String ipAddress = "8.8.8.8";
        IPResponse response;
        try {
            response = ipinfo.lookupIP(ipAddress);
        } catch (RateLimitedException e) {
            return null;
        }

        String latitude = response.getLatitude();
        String longitude = response.getLongitude();
        String city = response.getCity();
        String country = response.getCountryCode();

        String url = METEOMATICS_API_URL
                .replace("{datetime}", "now")
                .replace("{parameters}", "t_2m:C")
                .replace("{latitude}", latitude)
                .replace("{longitude}", longitude);

        WeatherResponseDto weatherResponse = restTemplate.getForObject(url, WeatherResponseDto.class);

        if (weatherResponse == null) {
            return null;
        }

        Double temperature = weatherResponse.getTemperature();
        String timestamp = weatherResponse.getData().get(0).getCoordinates().get(0).getDates().get(0).getDate();

        CompWeatherResponseDto compResponse = new CompWeatherResponseDto();
        compResponse.setTemperature(temperature);
        compResponse.setLocation(city + ", " + country);
        compResponse.setTimestamp(timestamp);

        return compResponse;
    }
}
