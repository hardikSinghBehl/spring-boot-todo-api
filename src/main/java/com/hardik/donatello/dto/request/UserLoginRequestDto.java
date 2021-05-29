package com.hardik.donatello.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JacksonStdImpl
public class UserLoginRequestDto {

	@Schema(description = "Email-id of user", required = true, example = "hardik.behl7444@gmail.com")
	@NotBlank
	@Email
	@Size(max = 50)
	private final String emailId;

	@Schema(description = "Password of user", required = true)
	@NotBlank
	@Size(min = 5, max = 15)
	private final String password;

}
