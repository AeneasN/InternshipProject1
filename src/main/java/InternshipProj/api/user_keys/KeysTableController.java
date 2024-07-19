package InternshipProj.api.user_keys;

import InternshipProj.api.users.Userid;
import InternshipProj.exception.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keys")
public class KeysTableController {

    @Autowired
    private KeysTableService keysTableService;

    // Endpoint to check if a key is valid for a specific API
    @InternshipProj.api.annotations.CheckDailyLimit
    @GetMapping("/check")
    public ResponseEntity<Boolean> isKeyForAPI(@RequestParam String key, @RequestParam String api) {
        boolean result = keysTableService.isKeyForAPI(key, api);
        return ResponseEntity.ok(result);
    }

    // Endpoint to check if a user is registered by their userId
    @InternshipProj.api.annotations.CheckDailyLimit
    @GetMapping("/user/check")
    public ResponseEntity<Boolean> isUserRegistered(@RequestParam Long userId) {
        boolean result = keysTableService.isUserRegistered(userId);
        return ResponseEntity.ok(result);
    }

    // Endpoint to get all API keys associated with a specific userId
    @GetMapping("/{userId}")
    @InternshipProj.api.annotations.CheckDailyLimit
    public ResponseEntity<List<KeysTable>> getAPIKeysByUserId(@PathVariable Long userId) {
        if (userId == null) {
            throw new OrderNotFoundException();
        }
        List<KeysTable> keysTables = keysTableService.getAPIKeysByUserId(userId);
        return ResponseEntity.ok(keysTables);
    }

    // Endpoint to delete an API key by its keysTableId
    @InternshipProj.api.annotations.CheckDailyLimit
    @DeleteMapping("/{keysTableId}")
    public ResponseEntity<Void> deleteAPIKey(@PathVariable Long keysTableId) {
        boolean deleted = keysTableService.deleteAPIKey(keysTableId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Endpoint to create a new API key
    @InternshipProj.api.annotations.CheckDailyLimit
    @PostMapping("/create")
    public KeysTable createKey(@RequestParam Long userId, @RequestParam String api) {
        return keysTableService.createKey(userId, api);
    }

    @InternshipProj.api.annotations.CheckDailyLimit
    @PutMapping("/toggle/{keysTableId}")
    public ResponseEntity<Void> toggleKeyActivation(@PathVariable Long keysTableId) {
        boolean toggled = keysTableService.toggleKeyActivation(keysTableId);
        return toggled ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @InternshipProj.api.annotations.CheckDailyLimit
    @GetMapping("/user/{userId}/status")
    public ResponseEntity<List<KeysTable>> getAPIKeysByUserIdAndActiveStatus(@PathVariable Long userId, @RequestParam boolean isActive) {
        List<KeysTable> keysTables = keysTableService.getKeysByActive(userId, isActive);
        return ResponseEntity.ok(keysTables);
    }
}
