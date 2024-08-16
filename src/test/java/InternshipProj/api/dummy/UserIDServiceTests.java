package InternshipProj.api.dummy;

import InternshipProj.api.dto.UserRequestDto;
import InternshipProj.api.spring_security.EmailService;
import InternshipProj.api.users.UserIDRepository;
import InternshipProj.api.users.UserIDService;
import InternshipProj.api.users.Userid;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserIDServiceTests {

    @Mock
    private UserIDRepository userIDRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserIDService userIDService;

    private Userid user;

    @BeforeEach
    void setUp() {
        user = new Userid("ex", "ex@email.com");
        user.setId(1L);
        user.setCode("ABC");
    }

    @Test
    void testSaveUserID() throws MessagingException {
        // Arrange
        UserRequestDto userDto = new UserRequestDto("ex", "ex@email.com");

        UserIDService userIDServiceSpy = Mockito.spy(userIDService);
        Mockito.doReturn("ABC").when(userIDServiceSpy).genCode();

        when(userIDRepository.save(any(Userid.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Userid savedUser = userIDServiceSpy.saveUserID(userDto);

        // Assert
        assertNotNull(savedUser);
        assertEquals("ex", savedUser.getName());
        assertEquals("ex@email.com", savedUser.getEmail());
        assertEquals("ABC", savedUser.getCode());

        verify(emailService, times(1)).sendVerify(savedUser, "ABC");
    }

    @Test
    void testDeleteUserID_UserExists() {
        // Arrange
        when(userIDRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = userIDService.deleteUserID(1L);

        // Assert
        assertTrue(result);
        verify(userIDRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUserID_UserDoesNotExist() {
        // Arrange
        when(userIDRepository.existsById(1L)).thenReturn(false);

        // Act
        boolean result = userIDService.deleteUserID(1L);

        // Assert
        assertFalse(result);
        verify(userIDRepository, never()).deleteById(1L);
    }

    @Test
    void testCheckUsage_WithinLimit() {
        // Arrange
        user.setUses(50);
        user.setRequestLimit(100);
        when(userIDRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        boolean result = userIDService.checkUsage(1L);

        // Assert
        assertTrue(result);
        assertEquals(51, user.getUses());
        verify(userIDRepository, times(1)).save(user);
    }

    @Test
    void testCheckUsage_ExceedsLimit() {
        // Arrange
        user.setUses(100);
        user.setRequestLimit(100);
        when(userIDRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        boolean result = userIDService.checkUsage(1L);

        // Assert
        assertFalse(result);
        assertEquals(100, user.getUses());
        verify(userIDRepository, never()).save(user);
    }

    @Test
    void testVerifyUser_ValidCode() {
        // Arrange
        when(userIDRepository.findByCode("ABC")).thenReturn(Optional.of(user));

        // Act
        boolean result = userIDService.verifyUser("ABC");

        // Assert
        assertTrue(result);
        assertEquals(1, user.getVerified());
        assertNull(user.getCode());
        verify(userIDRepository, times(1)).save(user);
    }

    @Test
    void testVerifyUser_InvalidCode() {
        // Arrange
        when(userIDRepository.findByCode("INVALID")).thenReturn(Optional.empty());

        // Act
        boolean result = userIDService.verifyUser("INVALID");

        // Assert
        assertFalse(result);
        verify(userIDRepository, never()).save(any(Userid.class));
    }
}
