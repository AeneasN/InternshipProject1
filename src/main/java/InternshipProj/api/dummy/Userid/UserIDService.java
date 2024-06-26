package InternshipProj.api.dummy.Userid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
}
