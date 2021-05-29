package com.hardik.donatello.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hardik.donatello.constant.ApiConstant;
import com.hardik.donatello.dto.request.UserDetailUpdationRequestDto;
import com.hardik.donatello.dto.response.UserDetailDto;
import com.hardik.donatello.security.utility.JwtUtils;
import com.hardik.donatello.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = ApiConstant.BASE_USER_PATH)
public class UserController {

	private final UserService userService;
	private final JwtUtils jwtUtils;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Returns logged-in user's profile details")
	public ResponseEntity<UserDetailDto> userDetailsRetreivalHandler(
			@RequestHeader(name = "Authorization", required = true) @Parameter(hidden = true) final String token) {
		return userService.retrieve(jwtUtils.extractUserId(token.replace("Bearer ", "")));
	}

	@PutMapping(value = ApiConstant.DETAILS)
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Updates logged-in user's profile details")
	public ResponseEntity<?> userDeatilUpdationHandler(
			@RequestBody(required = true) final UserDetailUpdationRequestDto userDetailUpdationRequestDto,
			@RequestHeader(name = "Authorization", required = true) @Parameter(hidden = true) final String token) {
		return userService.update(jwtUtils.extractUserId(token.replace("Bearer ", "")), userDetailUpdationRequestDto);
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Deletes User's account from the system")
	public ResponseEntity<?> userAccountDeletionHandler(
			@RequestHeader(name = "Authorization", required = true) @Parameter(hidden = true) final String token) {
		return userService.delete(jwtUtils.extractUserId(token.replace("Bearer ", "")));
	}

}
