package com.hardik.donatello.exception.handler;

import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hardik.donatello.constant.ApiResponse;
import com.hardik.donatello.exception.InvalidUserIdException;

@ControllerAdvice
public class InvalidUserIdExceptionHandler {

	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ResponseBody
	@ExceptionHandler(InvalidUserIdException.class)
	public ResponseEntity<?> userIdNotValidExceptionHandler(InvalidUserIdException ex) {
		final var response = new JSONObject();
		response.put(ApiResponse.STATUS, ApiResponse.FAILURE_STATUS);
		response.put(ApiResponse.MESSAGE, ApiResponse.INVALID_USER_ID);
		response.put(ApiResponse.TIMESTAMP, LocalDateTime.now().toString());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.toString());
	}

}
