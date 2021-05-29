package com.hardik.donatello.dto.request;

import java.time.LocalDate;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JacksonStdImpl
public class TodoCreationRequest {

	@Schema(description = "Title of TODO", required = true, example = "Learn spring-boot")
	@NotBlank
	@Size(max = 100)
	private final String title;

	@Schema(description = "Description of Todo", required = true, example = "Through YT videos")
	@NotBlank
	@Size(max = 500)
	private final String description;

	@Schema(description = "Predicted due date of created Todo", required = true, example = "2021-05-14")
	@NotBlank
	@FutureOrPresent
	private final LocalDate dueDate;

}
