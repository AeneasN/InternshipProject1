package InternshipProj.api.dummy;

import InternshipProj.api.dummy.dto.ResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController {

    private final DummyService dummyService;

    public DummyController(DummyService dummyService) {
        this.dummyService = dummyService;
    }

    @GetMapping("/test")
    public String test(@RequestParam(value = "name", defaultValue = "Success") String name) {
        return dummyService.test(name);
    }

    @GetMapping("/test2")
    public ResponseDto test2() {
        return dummyService.getMyName();
    }

}
