package com.hardik.donatello.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hardik.donatello.constant.ApiConstant;
import com.hardik.donatello.dto.request.TodoCreationRequestDto;
import com.hardik.donatello.dto.request.TodoUpdationRequestDto;
import com.hardik.donatello.dto.response.TodoDto;
import com.hardik.donatello.security.utility.JwtUtils;
import com.hardik.donatello.service.TodoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = ApiConstant.BASE_TODO_PATH)
public class TodoController {

	private final TodoService todoService;
	private final JwtUtils jwtUtils;

	@GetMapping(value = ApiConstant.ALL)
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Returns all todos created by user")
	public List<TodoDto> todosReteivalHandler(
			@RequestHeader(name = "Authorization", required = true) @Parameter(hidden = true) final String token) {
		return todoService.retreive(jwtUtils.extractUserId(token.replace("Bearer ", "")));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Creates a todo for a user")
	public void todoCreatonHandler(@RequestBody(required = true) final TodoCreationRequestDto todoCreationRequestDto,
			@RequestHeader(name = "Authorization", required = true) @Parameter(hidden = true) final String token) {
		todoService.create(jwtUtils.extractUserId(token.replace("Bearer ", "")), todoCreationRequestDto);
	}

	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Updates description and/or due-date of todo")
	public void todoUpdationHandler(@RequestBody(required = true) final TodoUpdationRequestDto todoUpdationRequestDto,
			@RequestHeader(name = "Authorization", required = true) @Parameter(hidden = true) final String token) {
		todoService.update(jwtUtils.extractUserId(token.replace("Bearer ", "")), todoUpdationRequestDto);
	}

	@PutMapping("/{todoId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Marks todo as completed (in-active)")
	public void todoStatusUpdationHandler(@PathVariable(name = "todoId", required = true) final UUID todoId,
			@RequestHeader(name = "Authorization", required = true) @Parameter(hidden = true) final String token) {
		todoService.update(jwtUtils.extractUserId(token.replace("Bearer ", "")), todoId);
	}

	@DeleteMapping("/{todoId}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Deletes a todo")
	public void todoDeletionHandler(@PathVariable(name = "todoId", required = true) final UUID todoId,
			@RequestHeader(name = "Authorization", required = true) @Parameter(hidden = true) final String token) {
		todoService.remove(jwtUtils.extractUserId(token.replace("Bearer ", "")), todoId);
	}

}
