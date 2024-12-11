package com.in28minutes.learn_spring_security.user;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService{
	
	private final UserRepository userRepository;

	public CustomUserDetailService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<UserEntity> optionalUser =  userRepository.findByUsername(username);
		
		UserEntity userEntity = optionalUser
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
		
		return User.builder()
				 .username(userEntity.getUsername())
				 .password(userEntity.getPassword())
				 .build();
	}
	
}
