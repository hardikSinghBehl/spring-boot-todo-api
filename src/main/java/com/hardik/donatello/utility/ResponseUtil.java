package com.hardik.donatello.utility;

import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.hardik.donatello.constant.ApiResponse;

@Component
public class ResponseUtil {

	public ResponseEntity<?> userCreationSuccessResponse() {
		final var response = new JSONObject();
		response.put(ApiResponse.STATUS, ApiResponse.SUCCESS_STATUS);
		response.put(ApiResponse.MESSAGE, ApiResponse.SUCCESSFULL_ACCOUNT_CREATION);
		response.put(ApiResponse.TIMESTAMP, LocalDateTime.now().toString());
		return ResponseEntity.ok(response.toString());
	}

	public ResponseEntity<?> userCreationFailureResponse() {
		final var response = new JSONObject();
		response.put(ApiResponse.STATUS, ApiResponse.FAILURE_STATUS);
		response.put(ApiResponse.MESSAGE, ApiResponse.UNSUCCESSFULL_ACCOUNT_CREATION);
		response.put(ApiResponse.TIMESTAMP, LocalDateTime.now().toString());
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(response.toString());
	}

	public ResponseEntity<?> duplicateEmailIdResponse() {
		final var response = new JSONObject();
		response.put(ApiResponse.STATUS, ApiResponse.FAILURE_STATUS);
		response.put(ApiResponse.MESSAGE, ApiResponse.UNSUCCESSFULL_ACCOUNT_CREATION);
		response.put(ApiResponse.TIMESTAMP, LocalDateTime.now().toString());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.toString());
	}

	public ResponseEntity<?> loginSuccessResponse(String jwtToken) {
		final var response = new JSONObject();
		response.put(ApiResponse.STATUS, ApiResponse.SUCCESS_STATUS);
		response.put(ApiResponse.MESSAGE, ApiResponse.LOGIN_SUCCESS);
		response.put(ApiResponse.JWT, jwtToken);
		response.put(ApiResponse.TIMESTAMP, LocalDateTime.now().toString());
		return ResponseEntity.status(HttpStatus.OK).body(response.toString());
	}

	public ResponseEntity<?> wrongEmailIdProvided() {
		final var response = new JSONObject();
		response.put(ApiResponse.STATUS, ApiResponse.FAILURE_STATUS);
		response.put(ApiResponse.MESSAGE, ApiResponse.WRONG_EMAIL_ID);
		response.put(ApiResponse.TIMESTAMP, LocalDateTime.now().toString());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.toString());
	}

	public ResponseEntity<?> wrongPasswordProvided() {
		final var response = new JSONObject();
		response.put(ApiResponse.STATUS, ApiResponse.FAILURE_STATUS);
		response.put(ApiResponse.MESSAGE, ApiResponse.WRONG_PASSWORD);
		response.put(ApiResponse.TIMESTAMP, LocalDateTime.now().toString());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.toString());
	}

	public ResponseEntity<?> userDetailsUpdationSuccessResponse() {
		final var response = new JSONObject();
		response.put(ApiResponse.STATUS, ApiResponse.SUCCESS_STATUS);
		response.put(ApiResponse.MESSAGE, ApiResponse.USER_DETAIL_UPDATION_SUCCESS);
		response.put(ApiResponse.TIMESTAMP, LocalDateTime.now().toString());
		return ResponseEntity.status(HttpStatus.OK).body(response.toString());
	}

	public ResponseEntity<?> userPasswordUpdationSuccessResponse() {
		final var response = new JSONObject();
		response.put(ApiResponse.STATUS, ApiResponse.SUCCESS_STATUS);
		response.put(ApiResponse.MESSAGE, ApiResponse.USER_PASSWORD_UPDATION_SUCCESS);
		response.put(ApiResponse.TIMESTAMP, LocalDateTime.now().toString());
		return ResponseEntity.status(HttpStatus.OK).body(response.toString());
	}

	public ResponseEntity<?> genericFailureResponse() {
		final var response = new JSONObject();
		response.put(ApiResponse.STATUS, ApiResponse.FAILURE_STATUS);
		response.put(ApiResponse.MESSAGE, ApiResponse.GENERIC_FAILURE);
		response.put(ApiResponse.TIMESTAMP, LocalDateTime.now().toString());
		return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(response.toString());
	}

	public ResponseEntity<?> todoDeletionSuccessResponse() {
		final var response = new JSONObject();
		response.put(ApiResponse.STATUS, ApiResponse.SUCCESS_STATUS);
		response.put(ApiResponse.MESSAGE, ApiResponse.TODO_DELETION);
		response.put(ApiResponse.TIMESTAMP, LocalDateTime.now().toString());
		return ResponseEntity.status(HttpStatus.OK).body(response.toString());
	}

	public ResponseEntity<?> genericUnauthorizeResponse() {
		final var response = new JSONObject();
		response.put(ApiResponse.STATUS, ApiResponse.FAILURE_STATUS);
		response.put(ApiResponse.MESSAGE, ApiResponse.UNAUTHORIZE);
		response.put(ApiResponse.TIMESTAMP, LocalDateTime.now().toString());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response.toString());
	}

	public ResponseEntity<?> todoUpdationSuccessResponse() {
		final var response = new JSONObject();
		response.put(ApiResponse.STATUS, ApiResponse.SUCCESS_STATUS);
		response.put(ApiResponse.MESSAGE, ApiResponse.TODO_UPDATION_SUCCESS);
		response.put(ApiResponse.TIMESTAMP, LocalDateTime.now().toString());
		return ResponseEntity.status(HttpStatus.OK).body(response.toString());
	}

	public ResponseEntity<?> todoCreationSuccessResponse() {
		final var response = new JSONObject();
		response.put(ApiResponse.STATUS, ApiResponse.SUCCESS_STATUS);
		response.put(ApiResponse.MESSAGE, ApiResponse.TODO_CREATION_SUCCESS);
		response.put(ApiResponse.TIMESTAMP, LocalDateTime.now().toString());
		return ResponseEntity.status(HttpStatus.OK).body(response.toString());
	}

	public ResponseEntity<?> todoNotFoundResponse() {
		final var response = new JSONObject();
		response.put(ApiResponse.STATUS, ApiResponse.FAILURE_STATUS);
		response.put(ApiResponse.MESSAGE, ApiResponse.INVALID_TODO_ID);
		response.put(ApiResponse.TIMESTAMP, LocalDateTime.now().toString());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response.toString());
	}

}
