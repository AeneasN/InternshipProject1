//package InternshipProj.api.dummy;
//
//import InternshipProj.api.user_keys.KeysTable;
//import InternshipProj.api.user_keys.KeysTableController;
//import InternshipProj.api.user_keys.KeysTableRepository;
//import InternshipProj.api.user_keys.KeysTableService;
//import InternshipProj.api.users.UserIDRepository;
//import InternshipProj.api.spring_security.ApiConfig;
//import InternshipProj.api.spring_security.PersSecurityFilter;
//import InternshipProj.api.spring_security.SecurityConfig;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.verify;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(KeysTableController.class)
//@ContextConfiguration(classes = {
//        KeysTableController.class,
//        SecurityConfig.class,
//        ApiConfig.class,
//        PersSecurityFilter.class,
//})
//public class KeysTableControllerTests {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private KeysTableService keysTableService;
//
//    @MockBean
//    private UserIDRepository userIDRepository;
//
//    @MockBean
//    private KeysTableRepository keysTableRepository;
//
//    @Test
//    @SneakyThrows
//    public void testIsKeyForAPI() {
//        // Arrange
//        String key = "1.001";
//        String api = "someApi";
//        when(keysTableService.isKeyForAPI(anyString(), anyString())).thenReturn(true);
//
//        // Act & Assert
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/keys/check")
//                        .param("key", key)
//                        .param("api", api))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string("true"));
//
//        verify(keysTableService).isKeyForAPI(anyString(), anyString());
//    }
//
//    @Test
//    @WithMockUser
//    @SneakyThrows
//    public void testGetAPIKeysByUserId() {
//        // Arrange
//        Long userId = 1L;
//        List<KeysTable> keysTables = new ArrayList<>();
//        keysTables.add(new KeysTable()); // Add mock keysTable objects as needed
//        when(keysTableService.getAPIKeysByUserId(anyLong())).thenReturn(keysTables);
//
//        // Act & Assert
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/keys/{userId}", userId))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
//
//        verify(keysTableService).getAPIKeysByUserId(anyLong());
//    }
//
//    @Test
//    @WithMockUser
//    @SneakyThrows
//    public void testDeleteAPIKey() {
//        // Arrange
//        Long keysTableId = 1L;
//        when(keysTableService.deleteAPIKey(anyLong())).thenReturn(true);
//
//        // Act & Assert
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/keys/{keysTableId}", keysTableId))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isNoContent());
//
//        verify(keysTableService).deleteAPIKey(anyLong());
//    }
//
//    @Test
//    @SneakyThrows
//    public void testCreateKey() {
//        // Arrange
//        Long userId = 1L;
//        String api = "someApi";
//        KeysTable newKey = new KeysTable(); // Set required properties for the key
//        when(keysTableService.createKey(anyLong(), anyString())).thenReturn(newKey);
//
//        // Act & Assert
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/keys/create")
//                        .param("userId", userId.toString())
//                        .param("api", api))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk());
//
//        verify(keysTableService).createKey(anyLong(), anyString());
//    }
//
//    @Test
//    @SneakyThrows
//    public void testToggleKeyActivation() {
//        // Arrange
//        Long keysTableId = 1L;
//        when(keysTableService.toggleKeyActivation(anyLong())).thenReturn(true);
//
//        // Act & Assert
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/keys/toggle/{keysTableId}", keysTableId))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk());
//
//        verify(keysTableService).toggleKeyActivation(anyLong());
//    }
//
//    @Test
//    @SneakyThrows
//    public void testGetAPIKeysByUserIdAndActiveStatus() {
//        // Arrange
//        Long userId = 1L;
//        boolean isActive = true;
//        List<KeysTable> keysTables = new ArrayList<>();
//        keysTables.add(new KeysTable()); // Add mock keysTable objects as needed
//        when(keysTableService.getKeysByActive(anyLong(), anyBoolean())).thenReturn(keysTables);
//
//        // Act & Assert
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/keys/user/{userId}/status", userId)
//                        .param("isActive", Boolean.toString(isActive)))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray());
//
//        verify(keysTableService).getKeysByActive(anyLong(), anyBoolean());
//    }
//}
