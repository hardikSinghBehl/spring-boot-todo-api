package com.hardik.donatello.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hardik.donatello.dto.request.TodoCreationRequestDto;
import com.hardik.donatello.dto.request.TodoUpdationRequestDto;
import com.hardik.donatello.dto.response.TodoDto;
import com.hardik.donatello.entity.Todo;
import com.hardik.donatello.entity.User;
import com.hardik.donatello.repository.TodoRepository;
import com.hardik.donatello.repository.UserRepository;
import com.hardik.donatello.utility.ResponseUtil;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TodoService {

	private final TodoRepository todoRepository;
	private final UserRepository userRepository;
	private final ResponseUtil responseUtil;

	private User getUser(final UUID userId) {
		return userRepository.findById(userId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No User Exists WIth the Specified Id"));
	}

	private Todo getTodo(final UUID todoId) {
		return todoRepository.findById(todoId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Todo Exists WIth the Specified Id"));
	}

	public ResponseEntity<List<TodoDto>> retreive(final UUID userId) {
		return ResponseEntity.ok(getUser(userId).getTodos().parallelStream()
				.map(todo -> TodoDto.builder().createdAt(todo.getCreatedAt()).description(todo.getDescription())
						.dueDate(todo.getDueDate()).id(todo.getId()).isActive(todo.isActive()).title(todo.getTitle())
						.updatedAt(todo.getUpdatedAt())
						.isExpired(LocalDate.now().isAfter(todo.getDueDate()) ? true : false).build())
				.collect(Collectors.toList()));
	}

	public ResponseEntity<?> create(final UUID userId, final TodoCreationRequestDto todoCreationRequest) {
		final var user = getUser(userId);
		final var todo = new Todo();
		todo.setActive(true);
		todo.setDescription(todoCreationRequest.getDescription());
		todo.setTitle(todoCreationRequest.getTitle());
		todo.setDueDate(todoCreationRequest.getDueDate());
		todo.setUserId(user.getId());
		final var savedTodo = todoRepository.save(todo);
		if (savedTodo != null)
			return responseUtil.todoCreationSuccessResponse();
		else
			return responseUtil.genericFailureResponse();
	}

	public ResponseEntity<?> update(final UUID userId, final TodoUpdationRequestDto todoUpdationRequest) {
		final var todo = getTodo(todoUpdationRequest.getId());

		if (!todo.getUserId().equals(userId))
			return responseUtil.genericUnauthorizeResponse();

		todo.setDescription(todoUpdationRequest.getDescription());
		todo.setDueDate(todoUpdationRequest.getDueDate());
		final var updatedTodo = todoRepository.save(todo);
		if (updatedTodo != null)
			return responseUtil.todoUpdationSuccessResponse();
		else
			return responseUtil.genericFailureResponse();
	}

	public ResponseEntity<?> update(UUID userId, UUID todoId) {
		final var todo = getTodo(todoId);

		if (!todo.getUserId().equals(userId))
			return responseUtil.genericUnauthorizeResponse();

		todo.setActive(false);
		final var updatedTodo = todoRepository.save(todo);
		if (updatedTodo != null)
			return responseUtil.todoUpdationSuccessResponse();
		else
			return responseUtil.genericFailureResponse();
	}

	public ResponseEntity<?> remove(final UUID userId, final UUID todoId) {
		final var todo = getTodo(todoId);

		if (!todo.getUserId().equals(userId))
			return responseUtil.genericUnauthorizeResponse();

		todoRepository.deleteById(todoId);
		return responseUtil.todoDeletionSuccessResponse();
	}

}
