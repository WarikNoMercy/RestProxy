package kirill.rest.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import kirill.rest.app.models.User;


public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByUsername (String username);
}
