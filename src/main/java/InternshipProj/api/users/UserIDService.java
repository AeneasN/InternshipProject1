package InternshipProj.api.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserIDService{
    @Autowired
    private UserIDRepository userIDRepository;

    public List<Userid> getAllUserIDs() {
        List<Userid> users = new ArrayList<>();
        userIDRepository.findAll().forEach(users::add);
        return users;
    }

    public Userid saveUserID(Userid userid) {
        return userIDRepository.save(userid);
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

}
