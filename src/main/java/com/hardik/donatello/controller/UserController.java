package com.hardik.donatello.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hardik.donatello.dto.request.UserDetailUpdationRequestDto;
import com.hardik.donatello.dto.response.UserDetailDto;
import com.hardik.donatello.security.utility.JwtUtils;
import com.hardik.donatello.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final UserService userService;
	private final JwtUtils jwtUtils;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Returns logged-in user's profile details")
	public UserDetailDto userDetailsRetreivalHandler(
			@RequestHeader(name = "Authorization", required = true) @Parameter(hidden = true) final String token) {
		return userService.retrieve(jwtUtils.extractUserId(token.replace("Bearer ", "")));
	}

	@PutMapping("/detail")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Updates logged-in user's profile details")
	public void userDeatilUpdationHandler(
			@RequestBody(required = true) final UserDetailUpdationRequestDto userDetailUpdationRequestDto,
			@RequestHeader(name = "Authorization", required = true) @Parameter(hidden = true) final String token) {
		userService.update(jwtUtils.extractUserId(token.replace("Bearer ", "")), userDetailUpdationRequestDto);
	}

}
