package com.hardik.donatello.dto.request;

import java.time.LocalDate;
import java.util.UUID;

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
public class TodoUpdationRequestDto {

	@Schema(description = "Id of TODO to be updated", required = true, example = "ca74bac4-253d-4c4f-a446-4e07fa338810")
	@NotBlank
	private final UUID id;

	@Schema(description = "Description of Todo", required = true, example = "Through YT videos")
	@NotBlank
	@Size(max = 500)
	private final String description;

	@Schema(description = "Predicted due date of created Todo", required = true, example = "2021-05-14")
	@NotBlank
	@FutureOrPresent
	private final LocalDate dueDate;

}
