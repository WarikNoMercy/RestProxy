package kirill.rest.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import kirill.rest.app.config.MyUserDetails;
import kirill.rest.app.models.User;
import kirill.rest.app.repositories.UserRepository;

public class MyUserDetailService implements UserDetailsService{

	@Autowired
	private UserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = repository.findByUsername(username);
		return user.map(MyUserDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException(username + " not found"));
	}

}
