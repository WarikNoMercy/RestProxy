package kirill.rest.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import kirill.rest.app.models.Audit;

public interface AuditRepository extends JpaRepository<Audit,Long>{

	
}
