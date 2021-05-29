package com.hardik.donatello.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JacksonStdImpl
public class UserDetailUpdationRequestDto {

	@Schema(description = "Firstname of user", required = true, example = "Hardik Singh")
	@NotBlank(message = "Firstname must not be empty")
	@Size(max = 50, message = "Firstname should not exceed more than 50 characters")
	private final String firstName;

	@Schema(description = "Lastname of user", required = true, example = "Behl")
	@NotBlank(message = "Lastname must not be empty")
	@Size(max = 50, message = "Lastname should not exceed more than 50 characters")
	private final String lastName;
}
