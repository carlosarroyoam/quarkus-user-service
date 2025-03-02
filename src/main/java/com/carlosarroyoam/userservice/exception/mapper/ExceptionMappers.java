package com.carlosarroyoam.userservice.exception.mapper;

import com.carlosarroyoam.userservice.dto.AppExceptionDto;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

class ExceptionMappers {
  private final UriInfo uriInfo;
  private final Clock clock;

  @Inject
  public ExceptionMappers(UriInfo uriInfo, Clock clock) {
    this.uriInfo = uriInfo;
    this.clock = clock;
  }

  @ServerExceptionMapper
  public Response mapWebApplicationException(WebApplicationException ex) {
    Status status = Status.fromStatusCode(ex.getResponse().getStatus());

    AppExceptionDto appExceptionDto = AppExceptionDto.builder()
        .message(ex.getMessage())
        .code(status.getStatusCode())
        .status(status.getReasonPhrase())
        .path(uriInfo.getPath())
        .timestamp(ZonedDateTime.now(clock))
        .build();

    return Response.status(status).entity(appExceptionDto).type(MediaType.APPLICATION_JSON).build();
  }

  @ServerExceptionMapper
  public Response mapConstraintViolationException(ConstraintViolationException ex) {
    Status status = Status.BAD_REQUEST;

    AppExceptionDto appExceptionDto = AppExceptionDto.builder()
        .message("Request data is not valid")
        .code(status.getStatusCode())
        .status(status.getReasonPhrase())
        .path(uriInfo.getPath())
        .timestamp(ZonedDateTime.now(clock))
        .details(ex.getConstraintViolations()
            .stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.toSet()))
        .build();

    return Response.status(status).entity(appExceptionDto).type(MediaType.APPLICATION_JSON).build();
  }

  @ServerExceptionMapper
  public Response mapException(Exception ex) {
    Status status = Status.INTERNAL_SERVER_ERROR;

    AppExceptionDto appExceptionDto = AppExceptionDto.builder()
        .message("Whoops! Something went wrong")
        .code(status.getStatusCode())
        .status(status.getReasonPhrase())
        .path(uriInfo.getPath())
        .timestamp(ZonedDateTime.now(clock))
        .build();

    ex.printStackTrace();

    return Response.status(status).entity(appExceptionDto).type(MediaType.APPLICATION_JSON).build();
  }
}