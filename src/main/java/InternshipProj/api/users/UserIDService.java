package InternshipProj.api.users;

import InternshipProj.api.dto.UserRequestDto;
import InternshipProj.api.spring_security.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserIDService{

    @Autowired
    private UserIDRepository userIDRepository;
    @Autowired
    private EmailService emailService;

    public List<Userid> getAllUserIDs() {
        List<Userid> users = new ArrayList<>();
        userIDRepository.findAll().forEach(users::add);
        return users;
    }

    public Userid saveUserID(UserRequestDto userdto) throws MessagingException {
        String code = genCode();
        Userid userid1 = new Userid(userdto.getName(), userdto.getEmail());
        userid1.setCode(code);
        userid1.setVerified(0);
        emailService.sendVerify(userid1, code);
        return userIDRepository.save(userid1);
    }

    public boolean deleteUserID(Long id) {
        if (userIDRepository.existsById(id)) {
            userIDRepository.deleteById(id);
            return true;
        }
        return false;
    }
    @Transactional
    public boolean checkUsage(Long userId){
        Userid user = userIDRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//check if exceeds limit
        if (user.getUses() >= user.getRequestLimit()){
            return false;
        }

        user.setUses(user.getUses() + 1);
        userIDRepository.save(user);
        return true;
    }

    @Transactional
    public boolean verifyUser(String code) {
        Optional<Userid> userid2 = userIDRepository.findByCode(code);
        if (userid2.isPresent()) {
            Userid user = userid2.get();
            user.setVerified(1);
            user.setCode(null);  // Clear the verification code after successful verification
            userIDRepository.save(user);
            return true;
        }
        return false;
    }

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private String genCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder(3);
        for (int i = 0; i < 3; i++) {
            code.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return code.toString();
    }


}
