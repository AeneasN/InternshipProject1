package InternshipProj.api.dummy;

import InternshipProj.api.dummy.dto.ResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class DummyServiceTest {

    @Test
    void test1() {
    }

    @Test
    void getMyName() {
    }

    @InjectMocks
    private DummyService dummyService;

    @Test
    void testTestMethod() {
        String name = "JUnit";
        String expected = "Test JUnit";

        String result = dummyService.test(name);

        assertEquals(expected, result);
    }

    @Test
    void testGetMyName() {
        ResponseDto response = dummyService.getMyName();

        assertEquals("My name is DummyService", response.getMessage());
    }

}
