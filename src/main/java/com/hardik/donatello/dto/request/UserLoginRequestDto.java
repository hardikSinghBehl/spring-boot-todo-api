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
	@NotBlank(message = "Email-id must not be empty")
	@Email(message = "Email-id entered is not of valid format")
	@Size(max = 50, message = "Email-id should not exceed more than 50 characters")
	private final String emailId;

	@Schema(description = "Password of user", required = true)
	@NotBlank(message = "Password must not be empty")
	@Size(min = 5, max = 15, message = "Password lenght should be between 5-15 characters")
	private final String password;

}
