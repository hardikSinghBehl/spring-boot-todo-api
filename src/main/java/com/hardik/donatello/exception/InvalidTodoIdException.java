package com.hardik.donatello.exception;

public class InvalidTodoIdException extends RuntimeException {

	private static final long serialVersionUID = 2594976354381076873L;

	public InvalidTodoIdException() {
		super();
	}

	public InvalidTodoIdException(String message) {
		super(message);
	}

}
