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
	@NotBlank
	@Size(max = 50)
	private final String firstName;

	@Schema(description = "Lastname of user", required = true, example = "Behl")
	@NotBlank
	@Size(max = 50)
	private final String lastName;
}
