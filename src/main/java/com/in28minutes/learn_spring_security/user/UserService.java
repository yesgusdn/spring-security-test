package com.in28minutes.learn_spring_security.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	public Optional<UserEntity> getByUsername(String username) {
		return userRepository.findByUsername(username);
	}

}
