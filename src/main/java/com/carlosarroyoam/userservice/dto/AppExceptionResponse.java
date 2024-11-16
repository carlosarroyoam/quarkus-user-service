package com.carlosarroyoam.userservice.dto;

import java.time.ZonedDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class AppExceptionResponse {
	private String message;
	private Set<String> details;
	private Integer code;
	private String status;
	private String path;
	private ZonedDateTime timestamp;
}
