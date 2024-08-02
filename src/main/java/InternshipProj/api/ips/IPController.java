package InternshipProj.api.ips;

import InternshipProj.api.dto.CompWeatherResponseDto;
import InternshipProj.api.dto.WeatherResponseDto;
import InternshipProj.api.users.UserIDRepository;
import InternshipProj.api.users.Userid;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class IPController{

    private final IPService ipService;
    private final UserIDRepository userIDRepository;

    @GetMapping("location")
    public IPTable getLocation(HttpServletRequest request, @RequestParam Long userId) {
        Optional<Userid> user = userIDRepository.findById(userId);
        String clientIp = "128.243.80.167";
//        String clientIp = request.getRemoteAddr();
        return ipService.getIpLocation(user.orElse(null), clientIp);
    }

    @GetMapping("weather")
    public CompWeatherResponseDto getWeather(@RequestParam Long userId){
        Optional<Userid> user = userIDRepository.findById(userId);
        return ipService.getWeatherForUser(user.orElse(null));
    }
    private Userid getUserById(Long userId) {
        return null;
    }
}
