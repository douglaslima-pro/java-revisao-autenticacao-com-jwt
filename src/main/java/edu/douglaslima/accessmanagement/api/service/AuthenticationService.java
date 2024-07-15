package edu.douglaslima.accessmanagement.api.service;

import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.douglaslima.accessmanagement.api.dto.TokenDTO;
import edu.douglaslima.accessmanagement.api.domain.user.User;
import edu.douglaslima.accessmanagement.api.dto.LoginDTO;
import edu.douglaslima.accessmanagement.api.dto.RegistrationDTO;
import edu.douglaslima.accessmanagement.api.infra.security.TokenService;
import edu.douglaslima.accessmanagement.api.repository.UserRepository;
import io.jsonwebtoken.lang.Arrays;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final AuthenticationManager authenticationManager;
	private final TokenService tokenService;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	public TokenDTO login(LoginDTO loginDTO) {
		try {
			UsernamePasswordAuthenticationToken userAuthentication = new UsernamePasswordAuthenticationToken(
					loginDTO.username(), loginDTO.password());
			Authentication userAuthenticated = authenticationManager.authenticate(userAuthentication);
			UserDetails userDetails = (UserDetails) userAuthenticated.getPrincipal();
			String token = tokenService.generateToken(userDetails);
			return new TokenDTO(token);
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException(e.getMessage());
		}
	}
	
	public User register(RegistrationDTO registrationDTO) throws IllegalArgumentException {
		if (userRepository.existsByUsername(registrationDTO.username())) {
			throw new IllegalArgumentException("Username already exists.");
		}
		if (userRepository.existsByEmail(registrationDTO.email())) {
			throw new IllegalArgumentException("E-mail already exists.");
		}
		User user = new User();
		user.setName(registrationDTO.name());
		user.setUsername(registrationDTO.username());
		user.setEmail(registrationDTO.email());
		user.setPassword(passwordEncoder.encode(registrationDTO.password()));
		user.setRoles(new ArrayList<>(Arrays.asList(new String[] {"USER"})));
		userRepository.save(user);
		return user;
	}
}
