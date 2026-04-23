package com.carlosarroyoam.userservice.core.exception.mapper.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.ZonedDateTime;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
@Builder
public class AppExceptionDto {
  private String message;
  private Integer code;
  private Set<String> details;
  private String status;
  private String path;
  private ZonedDateTime timestamp;
}
