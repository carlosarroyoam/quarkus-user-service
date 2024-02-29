package com.carlosarroyoam.userservice.exception.mapper;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import com.carlosarroyoam.userservice.dto.AppExceptionResponse;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;

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

		AppExceptionResponse appExceptionResponse = new AppExceptionResponse();
		appExceptionResponse.setMessage(ex.getMessage());
		appExceptionResponse.setCode(status.getStatusCode());
		appExceptionResponse.setStatus(status.getReasonPhrase());
		appExceptionResponse.setPath(uriInfo.getPath());
		appExceptionResponse.setTimestamp(ZonedDateTime.now(clock));

		return Response.status(status).entity(appExceptionResponse).type(MediaType.APPLICATION_JSON).build();
	}

	@ServerExceptionMapper
	public Response mapConstraintViolationException(ConstraintViolationException ex) {
		Status status = Status.BAD_REQUEST;

		AppExceptionResponse appExceptionResponse = new AppExceptionResponse();
		appExceptionResponse.setMessage("Request data is not valid");
		appExceptionResponse.setCode(status.getStatusCode());
		appExceptionResponse.setStatus(status.getReasonPhrase());
		appExceptionResponse.setPath(uriInfo.getPath());
		appExceptionResponse.setTimestamp(ZonedDateTime.now(clock));
		appExceptionResponse.setDetails(
				ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet()));

		return Response.status(status).entity(appExceptionResponse).type(MediaType.APPLICATION_JSON).build();
	}

	@ServerExceptionMapper
	public Response mapException(Exception ex) {
		Status status = Status.INTERNAL_SERVER_ERROR;

		AppExceptionResponse appExceptionResponse = new AppExceptionResponse();
		appExceptionResponse.setMessage("Whoops! Something went wrong");
		appExceptionResponse.setCode(status.getStatusCode());
		appExceptionResponse.setStatus(status.getReasonPhrase());
		appExceptionResponse.setPath(uriInfo.getPath());
		appExceptionResponse.setTimestamp(ZonedDateTime.now(clock));

		ex.printStackTrace();

		return Response.status(status).entity(appExceptionResponse).type(MediaType.APPLICATION_JSON).build();
	}

}