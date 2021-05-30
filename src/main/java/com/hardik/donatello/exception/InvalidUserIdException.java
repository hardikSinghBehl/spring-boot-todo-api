package com.hardik.donatello.exception;

public class InvalidUserIdException extends RuntimeException {

	private static final long serialVersionUID = 5275514233197559030L;

	public InvalidUserIdException() {
		super();
	}

	public InvalidUserIdException(String message) {
		super(message);
	}

}
