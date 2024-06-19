package InternshipProj.api.dummy;

import InternshipProj.api.dummy.dto.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public class DummyService {

    public String test(String name) {
        return String.format("Test %s", name);
    }

    public ResponseDto getMyName() {
        var response = new ResponseDto();
        response.setMessage("My name is DummyService");
        return response;
    }
}
