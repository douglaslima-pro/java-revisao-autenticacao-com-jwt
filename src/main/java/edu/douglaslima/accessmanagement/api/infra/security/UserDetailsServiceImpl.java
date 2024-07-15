package edu.douglaslima.accessmanagement.api.infra.security;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.douglaslima.accessmanagement.api.domain.user.User;
import edu.douglaslima.accessmanagement.api.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> foundUser = userRepository.findByUsername(username);
		User user = foundUser.orElseThrow(() -> new UsernameNotFoundException("The user was not found by 'username' parameter."));
		Collection<? extends GrantedAuthority> authorities = user.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority("ROLE_" + role))
				.toList();
		return org.springframework.security.core.userdetails.User.builder()
				.username(user.getUsername())
				.password(user.getPassword())
				.authorities(authorities)
				.build();
	}

}
