package InternshipProj.api.ips;

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
        String clientIp = request.getRemoteAddr();
        return ipService.getIpLocation(user.orElse(null), clientIp);
    }


    private Userid getUserById(Long userId) {
        return null;
    }
}
