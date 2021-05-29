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
public class TodoCreationRequestDto {

	@Schema(description = "Title of TODO", required = true, example = "Learn spring-boot")
	@NotBlank(message = "Title must not be empty")
	@Size(max = 100, message = "Title cannot exceed more than 100 characters")
	private final String title;

	@Schema(description = "Description of Todo", required = true, example = "Through YT videos")
	@NotBlank(message = "Description must not be empty")
	@Size(max = 500, message = "Description cannot exceed more than 500 characters")
	private final String description;

	@Schema(description = "Predicted due date of created Todo", required = true, example = "2021-05-14")
	@NotBlank(message = "Due-date must not be empty")
	@FutureOrPresent(message = "Due Date cannot be a past date")
	private final LocalDate dueDate;

}
