package com.hardik.donatello.service;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hardik.donatello.dto.request.UserCreationRequestDto;
import com.hardik.donatello.dto.request.UserDetailUpdationRequestDto;
import com.hardik.donatello.dto.request.UserLoginRequestDto;
import com.hardik.donatello.dto.request.UserPasswordUpdationRequestDto;
import com.hardik.donatello.dto.response.UserDetailDto;
import com.hardik.donatello.entity.User;
import com.hardik.donatello.repository.UserRepository;
import com.hardik.donatello.security.utility.JwtUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtils jwtUtils;

	private User getUser(final UUID userId) {
		return userRepository.findById(userId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No User Exists WIth the Specified Id"));
	}

	public UserDetailDto retrieve(final UUID userId) {
		final var user = getUser(userId);
		return UserDetailDto.builder().emailId(user.getEmail()).firstName(user.getFirstName())
				.lastName(user.getLastName()).createdAt(user.getCreatedAt()).updatedAt(user.getUpdatedAt()).build();
	}

	public void create(final UserCreationRequestDto userCreationRequest) {

		if (userRepository.existsByEmail(userCreationRequest.getEmailId()))
			throw new ResponseStatusException(HttpStatus.CONFLICT, "User Already Exists Woth Specified Email Id");

		final var user = new User();
		user.setEmail(userCreationRequest.getEmailId());
		user.setFirstName(userCreationRequest.getFirstName());
		user.setLastName(userCreationRequest.getLastName());
		user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
		userRepository.save(user);
	}

	public ResponseEntity<?> login(final UserLoginRequestDto userLoginRequest) {
		final var user = userRepository.findByEmail(userLoginRequest.getEmailId());
		if (user.isEmpty())
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong Email-id Provided");
		if (passwordEncoder.matches(userLoginRequest.getPassword(), user.get().getPassword())) {
			final var retreivedUser = user.get();
			final var jwtToken = jwtUtils.generateToken(retreivedUser);
			return ResponseEntity.ok(jwtToken);
		}
		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong Password Provided");
	}

	public void update(final UUID userId, final UserDetailUpdationRequestDto userDetailUpdationRequest) {
		final var user = getUser(userId);
		user.setFirstName(userDetailUpdationRequest.getFirstName());
		user.setLastName(userDetailUpdationRequest.getLastName());
		userRepository.save(user);
	}

	public void update(UUID userId, UserPasswordUpdationRequestDto userPasswordUpdationRequest) {
		final var user = getUser(userId);
		if (!passwordEncoder.matches(userPasswordUpdationRequest.getOldPassword(), user.getPassword()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong Password Provided");

		user.setPassword(passwordEncoder.encode(userPasswordUpdationRequest.getNewPassword()));
		userRepository.save(user);
	}

}
