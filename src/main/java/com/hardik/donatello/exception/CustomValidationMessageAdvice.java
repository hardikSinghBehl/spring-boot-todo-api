package com.hardik.donatello.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hardik.donatello.constant.ApiResponse;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomValidationMessageAdvice {

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		String errorMessage = fieldErrors.get(0).getDefaultMessage();

		final var response = new JSONObject();
		response.put(ApiResponse.STATUS, ApiResponse.FAILURE_STATUS);
		response.put(ApiResponse.MESSAGE, errorMessage);
		response.put(ApiResponse.TIMESTAMP, LocalDateTime.now().toString());
		return ResponseEntity.badRequest().body(response.toString());
	}

}