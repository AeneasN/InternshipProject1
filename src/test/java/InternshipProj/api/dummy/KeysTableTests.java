package InternshipProj.api.dummy;

import InternshipProj.api.dummy.Userid.UserIDRepository;
import InternshipProj.api.dummy.Userkeys.KeysTable;
import InternshipProj.api.dummy.Userkeys.KeysTableRepository;
import InternshipProj.api.dummy.Userkeys.KeysTableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KeysTableTests {

    @Mock
    private KeysTableRepository keysTableRepository;

    @Mock
    private UserIDRepository userIDRepository;

    @InjectMocks
    private KeysTableService keysTableService;

    private KeysTable key1;
    private KeysTable key2;

    @BeforeEach
    void setUp() {
        key1 = new KeysTable(1L, "key1", 1L, "api1");
        key2 = new KeysTable(2L, "key2", 1L, "api2");
    }

    @Test
    void testSave() {
        when(keysTableRepository.save(key1)).thenReturn(key1);

        KeysTable savedKey = keysTableService.saveAPIKey(key1);

        assertNotNull(savedKey);
        assertEquals("key1", savedKey.getKey());

        verify(keysTableRepository, times(1)).save(key1);
    }

    @Test
    void testDeleteIf() {
        when(keysTableRepository.existsById(1L)).thenReturn(true);
        doNothing().when(keysTableRepository).deleteById(1L);

        boolean result = keysTableService.deleteAPIKey(1L);

        assertTrue(result);

        verify(keysTableRepository, times(1)).existsById(1L);
        verify(keysTableRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteIfNot() {
        when(keysTableRepository.existsById(1L)).thenReturn(false);

        boolean result = keysTableService.deleteAPIKey(1L);

        assertFalse(result);

        verify(keysTableRepository, times(1)).existsById(1L);
        verify(keysTableRepository, never()).deleteById(1L);
    }

    @Test
    void testAPI() {
        when(keysTableRepository.findByKeyAndApi("key1", "api1")).thenReturn(Optional.of(key1));

        boolean result = keysTableService.isKeyForAPI("key1", "api1");

        assertTrue(result);

        verify(keysTableRepository, times(1)).findByKeyAndApi("key1", "api1");
    }

    @Test
    void testRegister() {
        when(userIDRepository.existsById(1L)).thenReturn(true);

        boolean result = keysTableService.isUserRegistered(1L);

        assertTrue(result);

        verify(userIDRepository, times(1)).existsById(1L);
    }

    @Test
    void testGetAPI() {
        List<KeysTable> keysList = new ArrayList<>();
        keysList.add(key1);
        keysList.add(key2);

        when(keysTableRepository.findByUserId(1L)).thenReturn(keysList);

        List<KeysTable> result = keysTableService.getAPIKeysByUserId(1L);

        assertEquals(2, result.size());
        assertEquals("key1", result.get(0).getKey());
        assertEquals("key2", result.get(1).getKey());

        verify(keysTableRepository, times(1)).findByUserId(1L);
    }
}
