package edu.douglaslima.accessmanagement.api.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.douglaslima.accessmanagement.api.domain.user.User;
import edu.douglaslima.accessmanagement.api.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User searchUser(String id) {
		Optional<User> foundUser = userRepository.findById(id);
		return foundUser.orElseThrow(() -> new NoSuchElementException(String.format("User ID %s was not found.", id)));
	}

	public List<User> searchAllUsers() {
		return userRepository.findAll();
	}

}
