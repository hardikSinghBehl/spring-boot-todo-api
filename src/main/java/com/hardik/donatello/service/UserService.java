package com.hardik.donatello.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hardik.donatello.dto.request.UserCreationRequestDto;
import com.hardik.donatello.dto.request.UserDetailUpdationRequestDto;
import com.hardik.donatello.dto.request.UserLoginRequestDto;
import com.hardik.donatello.dto.request.UserPasswordUpdationRequestDto;
import com.hardik.donatello.dto.response.UserDetailDto;
import com.hardik.donatello.entity.User;
import com.hardik.donatello.exception.InvalidUserIdException;
import com.hardik.donatello.repository.UserRepository;
import com.hardik.donatello.security.utility.JwtUtils;
import com.hardik.donatello.utility.ResponseUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtils jwtUtils;
	private final ResponseUtil responseUtil;

	private User getUser(final UUID userId) {
		return userRepository.findById(userId).orElseThrow(() -> new InvalidUserIdException());
	}

	public ResponseEntity<UserDetailDto> retrieve(final UUID userId) {
		final var user = getUser(userId);
		return ResponseEntity.ok(UserDetailDto.builder().emailId(user.getEmail()).firstName(user.getFirstName())
				.lastName(user.getLastName()).createdAt(user.getCreatedAt()).updatedAt(user.getUpdatedAt()).build());
	}

	public ResponseEntity<?> create(final UserCreationRequestDto userCreationRequest) {

		if (userRepository.existsByEmail(userCreationRequest.getEmailId()))
			return responseUtil.duplicateEmailIdResponse();

		final var user = new User();
		user.setEmail(userCreationRequest.getEmailId());
		user.setFirstName(userCreationRequest.getFirstName());
		user.setLastName(userCreationRequest.getLastName());
		user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
		final var savedUser = userRepository.save(user);

		if (savedUser != null)
			return responseUtil.userCreationSuccessResponse();
		else
			return responseUtil.userCreationFailureResponse();
	}

	public ResponseEntity<?> login(final UserLoginRequestDto userLoginRequest) {
		final var user = userRepository.findByEmail(userLoginRequest.getEmailId());
		if (user.isEmpty())
			return responseUtil.wrongEmailIdProvided();
		if (passwordEncoder.matches(userLoginRequest.getPassword(), user.get().getPassword())) {
			final var retreivedUser = user.get();
			final var jwtToken = jwtUtils.generateToken(retreivedUser);
			return responseUtil.loginSuccessResponse(jwtToken);
		}
		return responseUtil.wrongPasswordProvided();
	}

	public ResponseEntity<?> update(final UUID userId, final UserDetailUpdationRequestDto userDetailUpdationRequest) {
		final var user = getUser(userId);
		user.setFirstName(userDetailUpdationRequest.getFirstName());
		user.setLastName(userDetailUpdationRequest.getLastName());
		final var updatedUser = userRepository.save(user);
		if (updatedUser != null)
			return responseUtil.userDetailsUpdationSuccessResponse();
		else
			return responseUtil.genericFailureResponse();
	}

	public ResponseEntity<?> update(UUID userId, UserPasswordUpdationRequestDto userPasswordUpdationRequest) {
		final var user = getUser(userId);
		if (!passwordEncoder.matches(userPasswordUpdationRequest.getOldPassword(), user.getPassword()))
			return responseUtil.wrongPasswordProvided();

		user.setPassword(passwordEncoder.encode(userPasswordUpdationRequest.getNewPassword()));
		final var updatedUser = userRepository.save(user);
		if (updatedUser != null)
			return responseUtil.userPasswordUpdationSuccessResponse();
		else
			return responseUtil.genericFailureResponse();
	}

	public ResponseEntity<?> delete(UUID userId) {
		userRepository.deleteById(userId);
		return responseUtil.userDeletionSuccessResponse();
	}

}
