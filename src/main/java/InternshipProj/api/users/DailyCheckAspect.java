package InternshipProj.api.users;

import InternshipProj.api.users.UserIDService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;

@Aspect
@Component
public class DailyCheckAspect {

    @Autowired
    private UserIDService useridService;

    @Around("@annotation(InternshipProj.api.annotations.CheckDailyLimit) && args(userId,..)")
    public Object checkDailyLimit(ProceedingJoinPoint joinPoint, Long userId) throws Throwable {
        if (!useridService.checkUsage(userId)){
            return ResponseEntity.status(429).body("Daily limit reached");
        }
        return joinPoint.proceed();
    }
}
