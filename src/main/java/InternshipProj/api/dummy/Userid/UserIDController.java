package InternshipProj.api.dummy.Userid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userids")
public class UserIDController {

    @Autowired
    private UserIDService userIDService;

    @GetMapping
    public List<Userid> getAllUserKeys() {
        return userIDService.getAllUserIDs();
    }

    @PostMapping
    public Userid createUserKey(@RequestBody Userid userid) {
        return userIDService.saveUserID(userid);
    }

    @DeleteMapping("/{id}")
    public void deleteUserKey(@PathVariable Long id) {
        userIDService.deleteUserID(id);
    }
}
