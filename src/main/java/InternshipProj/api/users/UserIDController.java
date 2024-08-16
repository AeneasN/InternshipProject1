package InternshipProj.api.users;


import InternshipProj.api.dto.UserRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "Users Controller", description = "Manage List of Users")
public class UserIDController {

    @Autowired
    private UserIDService userIDService;


    @InternshipProj.api.annotations.CheckDailyLimit
    @GetMapping
    @Operation(description = "Provides List of Users")
    public List<Userid> getAllUserKeys() {
        return userIDService.getAllUserIDs();
    }

    @PostMapping
    @Operation(description = "Generates User IDs")
    @ResponseStatus(code = HttpStatus.CREATED)
    public boolean createUserKey(@RequestBody UserRequestDto userid) throws MessagingException {
        Userid userdto;
        userIDService.saveUserID(userid);
        return true;
    }

    @InternshipProj.api.annotations.CheckDailyLimit
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.CREATED)
    @Operation(description = "Delete Users")
    public void deleteUserKey(@PathVariable Long id) {
        userIDService.deleteUserID(id);
    }

    @InternshipProj.api.annotations.CheckDailyLimit
    @GetMapping("/api/verify")
    public String verifyUser(@RequestParam("code") String code) {
        boolean verified = userIDService.verifyUser(code);
        if (verified) {
            return "Email successfully verified!";
        } else {
            return "Invalid verification code.";
        }
    }
}
