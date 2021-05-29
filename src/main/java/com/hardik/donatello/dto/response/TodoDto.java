package com.hardik.donatello.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JacksonStdImpl
public class TodoDto {

	private final UUID id;
	private final String title;
	private final String description;
	private final LocalDate dueDate;
	private final Boolean isActive;
	private final Boolean isExpired;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;

}
