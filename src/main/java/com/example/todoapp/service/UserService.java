package com.example.todoapp.service;

import com.example.todoapp.model.User;
import com.example.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public User registerUser(User user) {

		// Check if username or email already exists
		if (userRepository.existsByUsername(user.getUsername())) {
			throw new IllegalArgumentException("Username already exists");
		}
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new IllegalArgumentException("Email already exists");
		}

		return userRepository.save(user);
	}

	public Optional<User> authenticateUser(String username, String password) {
		Optional<User> userOptional = userRepository.findByUsername(username);

		if (userOptional.isPresent()) {
			User user = userOptional.get();
			
			// compare
			if (user.getPassword().equals(password)) {
				return Optional.of(user);
			}
		}

		return Optional.empty();
	}

	public Optional<User> getUserById(Long id) {
		return userRepository.findById(id);
	}
}