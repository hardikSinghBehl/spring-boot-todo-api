package com.hardik.donatello.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JacksonStdImpl
public class UserDetailDto {

	private final String firstName;
	private final String lastName;
	private final String emailId;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;

}
