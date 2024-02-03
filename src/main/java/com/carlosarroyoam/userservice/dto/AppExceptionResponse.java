package com.carlosarroyoam.userservice.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class AppExceptionResponse implements Serializable {

	private static final long serialVersionUID = 1982149359567491484L;
	private String message;
	private Set<String> details;
	private Integer code;
	private String status;
	private String path;
	private ZonedDateTime timestamp;

}
