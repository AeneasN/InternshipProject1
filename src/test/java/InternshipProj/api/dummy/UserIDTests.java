package InternshipProj.api.dummy;

import InternshipProj.api.dummy.Userid.UserIDRepository;
import InternshipProj.api.dummy.Userid.UserIDService;
import InternshipProj.api.dummy.Userid.Userid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserIDTests {
    @Mock
    private UserIDRepository userIDRepository;

    @InjectMocks
    private UserIDService userIDService;

    private Userid user1;
    private Userid user2;

    @BeforeEach
    void setUp() {
        user1 = new Userid(1L, "user1", "user1@example.com");
        user2 = new Userid(2L, "user2", "user2@example.com");
    }

    @Test
    void testSave() {
        when(userIDRepository.save(user1)).thenReturn(user1);

        Userid savedUser = userIDService.saveUserID(user1);

        assertNotNull(savedUser);
        assertEquals("user1", savedUser.getUsername());

        verify(userIDRepository, times(1)).save(user1);
    }

    @Test
    void testDeleteIf() {
        when(userIDRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userIDRepository).deleteById(1L);

        boolean result = userIDService.deleteUserID(1L);

        assertTrue(result);

        verify(userIDRepository, times(1)).existsById(1L);
        verify(userIDRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteIfNot() {
        when(userIDRepository.existsById(1L)).thenReturn(false);

        boolean result = userIDService.deleteUserID(1L);

        assertFalse(result);

        verify(userIDRepository, times(1)).existsById(1L);
        verify(userIDRepository, never()).deleteById(1L);
    }
}
