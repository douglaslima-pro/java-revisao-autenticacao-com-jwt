package edu.douglaslima.accessmanagement.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.douglaslima.accessmanagement.api.domain.user.User;
import edu.douglaslima.accessmanagement.api.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("search/{id}")
	public ResponseEntity<User> searchUser(@PathVariable String id) {
		User user = userService.searchUser(id);
		return ResponseEntity.ok(user);
	}
	
	@GetMapping("search")
	public ResponseEntity<List<User>> searchAllUsers() {
		List<User> users = userService.searchAllUsers();
		return ResponseEntity.ok(users);
	}
	
}
