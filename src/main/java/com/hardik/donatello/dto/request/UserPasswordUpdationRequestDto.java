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
public class UserPasswordUpdationRequestDto {

	@Schema(description = "Current Password of user", required = true)
	@NotBlank
	@Size(min = 5, max = 15)
	private final String oldPassword;

	@Schema(description = "New Password of user", required = true)
	@NotBlank
	@Size(min = 5, max = 15)
	private final String newPassword;

}
