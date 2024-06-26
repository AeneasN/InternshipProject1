package InternshipProj.api.dummy.Userkeys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keys")
public class KeysTableController {

    @Autowired
    private KeysTableService keysTableService;

    // Endpoint to check if a key is valid for a specific API
    @GetMapping("/check")
    public ResponseEntity<Boolean> isKeyForAPI(@RequestParam String key, @RequestParam String api) {
        boolean result = keysTableService.isKeyForAPI(key, api);
        return ResponseEntity.ok(result);
    }

    // Endpoint to check if a user is registered by their userId
    @GetMapping("/user/check")
    public ResponseEntity<Boolean> isUserRegistered(@RequestParam Long userId) {
        boolean result = keysTableService.isUserRegistered(userId);
        return ResponseEntity.ok(result);
    }

    // Endpoint to get all API keys associated with a specific userId
    @GetMapping("/{userId}")
    public ResponseEntity<List<KeysTable>> getAPIKeysByUserId(@PathVariable Long userId) {
        List<KeysTable> keysTables = keysTableService.getAPIKeysByUserId(userId);
        return ResponseEntity.ok(keysTables);
    }

    // Endpoint to delete an API key by its keysTableId
    @DeleteMapping("/{keysTableId}")
    public ResponseEntity<Void> deleteAPIKey(@PathVariable Long keysTableId) {
        boolean deleted = keysTableService.deleteAPIKey(keysTableId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Endpoint to create a new API key
    @PostMapping("/create")
    public ResponseEntity<KeysTable> createAPIKey(@RequestBody KeysTable keysTable) {
        KeysTable savedKey = keysTableService.saveAPIKey(keysTable);
        return new ResponseEntity<>(savedKey, HttpStatus.CREATED);
    }
}
