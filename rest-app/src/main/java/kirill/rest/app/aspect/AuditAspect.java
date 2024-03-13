package kirill.rest.app.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import kirill.rest.app.models.User;
import kirill.rest.app.repositories.UserRepository;
import kirill.rest.app.services.AuditService;


@Aspect
@Component
public class AuditAspect {

	@Autowired
    private AuditService auditService;
	
	@Autowired
	private UserRepository userRepository;

    @Pointcut("execution(* kirill.rest.app.controllers.*.*(..))")
    public void controllerMethods() {}

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterMethodExecution(JoinPoint joinPoint, Object result) {
    	if(result instanceof User) {
    		User user = (User) result;
    		String methodName = joinPoint.getSignature().getName();
    		auditService.createAuditRecord(user, user.getUsername(), methodName, true);
    		return;
    	}else if (result instanceof ResponseEntity){
    		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication != null ? authentication.getName() : "anonymous";
            User user = userRepository.findByUsername(username).get();
            String methodName = joinPoint.getSignature().getName();   
    	    ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
    	    HttpStatusCode statusCode = responseEntity.getStatusCode();
    	    boolean success = statusCode != HttpStatus.FORBIDDEN;
            auditService.createAuditRecord(user, username, methodName, success);
    	}
    }
    
    
}
