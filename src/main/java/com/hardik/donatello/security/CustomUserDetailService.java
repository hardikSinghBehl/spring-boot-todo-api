package com.hardik.donatello.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hardik.donatello.repository.UserRepository;
import com.hardik.donatello.security.utility.SecurityUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
		return SecurityUtils.convert(userRepository.findByEmail(emailId)
				.orElseThrow(() -> new UsernameNotFoundException("Bad Credentials")));
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}