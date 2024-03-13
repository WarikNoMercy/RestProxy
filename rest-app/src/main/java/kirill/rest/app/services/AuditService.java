package kirill.rest.app.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import kirill.rest.app.repositories.AuditRepository;
import kirill.rest.app.models.Audit;
import kirill.rest.app.models.User;

@Service
public class AuditService {

	@Autowired
    private AuditRepository auditRepository;


	
    @Transactional
    public void createAuditRecord(User user, String username, String methodName, boolean hasAccess) {
        Audit audit = new Audit();
        audit.setUser(user);
        audit.setDate(LocalDateTime.now().toString());
        audit.setParams(methodName);
        audit.setUsername(username);
        audit.setHasAccess(hasAccess);
        auditRepository.save(audit);
    }
}
