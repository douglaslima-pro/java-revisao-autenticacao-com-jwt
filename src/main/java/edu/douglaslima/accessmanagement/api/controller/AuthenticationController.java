package edu.douglaslima.accessmanagement.api.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import edu.douglaslima.accessmanagement.api.domain.user.User;
import edu.douglaslima.accessmanagement.api.dto.LoginDTO;
import edu.douglaslima.accessmanagement.api.dto.RegistrationDTO;
import edu.douglaslima.accessmanagement.api.dto.TokenDTO;
import edu.douglaslima.accessmanagement.api.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationService authenticationService;
	
	@PostMapping("/login")
	public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) {
		TokenDTO tokenDTO = authenticationService.login(loginDTO);
		return ResponseEntity.accepted().body(tokenDTO);
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody RegistrationDTO registrationDTO) {
		User createdUser = authenticationService.register(registrationDTO);
		URI location = ServletUriComponentsBuilder.fromPath("/user/search")
				.path("/{id}")
				.buildAndExpand(createdUser.getId())
				.toUri();
		return ResponseEntity.created(location).body(String.format("User of ID %s was created successfully.", createdUser.getId()));
	}
	
}
