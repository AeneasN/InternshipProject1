package InternshipProj.api.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DailyReset {
    @Autowired
    private UserIDRepository userIDRepository;

    @Scheduled(cron= "0 0 0 * * ?")
    @Transactional
    public void resetUse(){
        userIDRepository.resetAllUses();
    }
}
