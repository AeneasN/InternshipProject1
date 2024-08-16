package InternshipProj.api.users;

import InternshipProj.api.dto.UserRequestDto;
import InternshipProj.api.spring_security.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserIDController.class)
public class UserIDControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserIDService userIDService;

    @MockBean
    private UserIDRepository userIDRepository; // Mocking repository if needed

    @MockBean
    private EmailService emailService; // Mocking email service

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    public void testGetAllUserKeys() throws Exception {
        // Arrange
        Userid user1 = new Userid("ex", "ex@email.com");
        Userid user2 = new Userid("ex2", "ex2@email.com");
        List<Userid> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        when(userIDService.getAllUserIDs()).thenReturn(users);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("ex"))
                .andExpect(jsonPath("$[0].email").value("ex@email.com"))
                .andExpect(jsonPath("$[1].name").value("ex2"))
                .andExpect(jsonPath("$[1].email").value("ex2@email.com"));

        verify(userIDService, times(1)).getAllUserIDs();
    }

//    @Test
//    @WithMockUser
//    public void testCreateUserKey() throws Exception {
//        // Arrange
//        UserRequestDto userDto = new UserRequestDto("ex", "ex@email.com");
//        Userid user = new Userid("ex", "ex@email.com");
//        when(userIDService.saveUserID(any(UserRequestDto.class))).thenReturn(user);
//
//        // Act & Assert
//        mockMvc.perform(post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(userDto)))
//                .andDo(print())
//                .andExpect(status().isCreated());  // Check if this is the correct status code
//
//        verify(userIDService, times(1)).saveUserID(any(UserRequestDto.class));
//    }
//
//    @Test
//    @WithMockUser
//    public void testDeleteUserKey() throws Exception {
//        // Arrange
//        Long userId = 1L;
//        doNothing().when(userIDService).deleteUserID(userId);  // Ensure this is correct
//
//        // Act & Assert
//        mockMvc.perform(delete("/users/{id}", userId))
//                .andDo(print())
//                .andExpect(status().isNoContent());  // Ensure this is the expected status code
//
//        verify(userIDService, times(1)).deleteUserID(userId);
//    }

    @Test
    @WithMockUser
    public void testVerifyUser() throws Exception {
        // Arrange
        String code = "ABC";
        when(userIDService.verifyUser(anyString())).thenReturn(true);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/users/api/verify")
                        .param("code", code))
                .andExpect(status().isOk())
                .andExpect(content().string("Email successfully verified!"));

        verify(userIDService, times(1)).verifyUser(anyString());
    }

    @Test
    @WithMockUser
    public void testVerifyUser_InvalidCode() throws Exception {
        // Arrange
        String code = "XYZ";
        when(userIDService.verifyUser(anyString())).thenReturn(false);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/users/api/verify")
                        .param("code", code))
                .andExpect(status().isOk())
                .andExpect(content().string("Invalid verification code."));

        verify(userIDService, times(1)).verifyUser(anyString());
    }
}
