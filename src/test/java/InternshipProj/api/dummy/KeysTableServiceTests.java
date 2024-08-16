package InternshipProj.api.dummy;

import InternshipProj.api.user_keys.KeysTable;
import InternshipProj.api.user_keys.KeysTableRepository;
import InternshipProj.api.user_keys.KeysTableService;
import InternshipProj.api.users.UserIDRepository;
import InternshipProj.api.users.Userid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class KeysTableServiceTests {

    @Mock
    private KeysTableRepository keysTableRepository;

    @Mock
    private UserIDRepository userIDRepository;

    @InjectMocks
    private KeysTableService keysTableService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateKey() {
        // Arrange
        Long userId = 1L;
        String api = "test-api";

        // Create a Userid instance
        Userid user = new Userid();
        user.setId(userId);

        // Set up mock behavior
        when(userIDRepository.findById(userId)).thenReturn(Optional.of(user));
        when(keysTableRepository.countByUser(user)).thenReturn(1L);
        when(keysTableRepository.save(any(KeysTable.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        KeysTable result = keysTableService.createKey(userId, api);

        // Assert
        assertNotNull(result);
        assertEquals("1.002", result.getKey());
        assertEquals(user, result.getUser());
        assertEquals(api, result.getApi());
        assertTrue(result.getIsActive());

        // Verify interactions
        verify(userIDRepository, times(1)).findById(userId);
        verify(keysTableRepository, times(1)).countByUser(user);
        verify(keysTableRepository, times(1)).save(any(KeysTable.class));
    }


    @Test
    public void testIsKeyForAPI() {
        // Arrange
        String key = "1.001";
        String api = "test-api";
        KeysTable keysTable = new KeysTable();
        keysTable.setKey(key);
        keysTable.setApi(api);

        when(keysTableRepository.findByKeyAndApi(key, api)).thenReturn(Optional.of(keysTable));

        // Act
        boolean result = keysTableService.isKeyForAPI(key, api);

        // Assert
        assertTrue(result);
        verify(keysTableRepository, times(1)).findByKeyAndApi(key, api);
    }

    @Test
    public void testIsUserRegistered() {
        // Arrange
        Long userId = 1L;
        when(userIDRepository.existsById(userId)).thenReturn(true);

        // Act
        boolean result = keysTableService.isUserRegistered(userId);

        // Assert
        assertTrue(result);
        verify(userIDRepository, times(1)).existsById(userId);
    }

    @Test
    public void testGetAPIKeysByUserId() {
        // Arrange
        Long userId = 1L;
        List<KeysTable> keysList = new ArrayList<>();
        KeysTable key = new KeysTable();
        key.setUser(new Userid());
        keysList.add(key);

        when(keysTableRepository.findByUserId(userId)).thenReturn(keysList);

        // Act
        List<KeysTable> result = keysTableService.getAPIKeysByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(keysTableRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testDeleteAPIKey() {
        // Arrange
        Long keyId = 1L;
        when(keysTableRepository.existsById(keyId)).thenReturn(true);

        // Act
        boolean result = keysTableService.deleteAPIKey(keyId);

        // Assert
        assertTrue(result);
        verify(keysTableRepository, times(1)).existsById(keyId);
        verify(keysTableRepository, times(1)).deleteById(keyId);
    }

    @Test
    public void testSaveAPIKey() {
        // Arrange
        KeysTable keysTable = new KeysTable();
        when(keysTableRepository.save(keysTable)).thenReturn(keysTable);

        // Act
        KeysTable result = keysTableService.saveAPIKey(keysTable);

        // Assert
        assertNotNull(result);
        verify(keysTableRepository, times(1)).save(keysTable);
    }

    @Test
    public void testToggleKeyActivation() {
        // Arrange
        Long keyId = 1L;
        KeysTable key = new KeysTable();
        key.setIsActive(true);
        when(keysTableRepository.findById(keyId)).thenReturn(Optional.of(key));
        when(keysTableRepository.save(any(KeysTable.class))).thenReturn(key);

        // Act
        boolean result = keysTableService.toggleKeyActivation(keyId);

        // Assert
        assertTrue(result);
        assertFalse(key.getIsActive()); // Should be toggled
        verify(keysTableRepository, times(1)).findById(keyId);
        verify(keysTableRepository, times(1)).save(any(KeysTable.class));
    }

    @Test
    public void testGetKeysByActive() {
        // Arrange
        Long userId = 1L;
        boolean isActive = true;
        boolean activeStatus = true;
        List<KeysTable> keysList = new ArrayList<>();
        KeysTable key = new KeysTable();
        key.setIsActive(activeStatus);
        keysList.add(key);
        Integer activeStatusB = 1;

        when(keysTableRepository.findByUserIdAndIsActive(userId, activeStatusB)).thenReturn(keysList);

        // Act
        List<KeysTable> result = keysTableService.getKeysByActive(userId, isActive);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(keysTableRepository, times(1)).findByUserIdAndIsActive(userId, activeStatusB);
    }
}
