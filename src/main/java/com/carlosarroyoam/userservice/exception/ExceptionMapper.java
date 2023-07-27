package com.carlosarroyoam.userservice.exception;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import com.carlosarroyoam.userservice.dto.AppExceptionDto;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;

class ExceptionMappers {

	private static final Logger LOG = Logger.getLogger(ExceptionMappers.class);
	private final UriInfo uriInfo;
	private final Clock clock;

	@Inject
	public ExceptionMappers(UriInfo uriInfo, Clock clock) {
		this.uriInfo = uriInfo;
		this.clock = clock;
	}

	@ServerExceptionMapper
	public Response mapWebApplicationException(WebApplicationException ex) {
		AppExceptionDto appExceptionDto = new AppExceptionDto();
		appExceptionDto.setMessage(ex.getMessage());
		appExceptionDto.setCode(ex.getResponse().getStatus());
		appExceptionDto.setStatus(ex.getResponse().getStatusInfo().getReasonPhrase());
		appExceptionDto.setPath(uriInfo.getPath());
		appExceptionDto.setTimestamp(ZonedDateTime.now(clock));

		LOG.error(ex.getMessage());

		return Response.status(ex.getResponse().getStatus()).entity(appExceptionDto).build();
	}

	@ServerExceptionMapper
	public Response mapConstraintViolationException(ConstraintViolationException ex) {
		AppExceptionDto appExceptionDto = new AppExceptionDto();
		appExceptionDto.setMessage(Status.BAD_REQUEST.getReasonPhrase());
		appExceptionDto.setDetails(
				ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet()));
		appExceptionDto.setCode(Status.BAD_REQUEST.getStatusCode());
		appExceptionDto.setStatus(Status.BAD_REQUEST.getReasonPhrase());
		appExceptionDto.setPath(uriInfo.getPath());
		appExceptionDto.setTimestamp(ZonedDateTime.now(clock));

		LOG.error(appExceptionDto.getDetails());

		return Response.status(Status.BAD_REQUEST.getStatusCode()).entity(appExceptionDto).build();
	}

	@ServerExceptionMapper
	public Response mapException(Exception ex) {
		AppExceptionDto appExceptionDto = new AppExceptionDto();
		appExceptionDto.setMessage("Whoops! Something went wrong");
		appExceptionDto.setCode(Status.INTERNAL_SERVER_ERROR.getStatusCode());
		appExceptionDto.setStatus(Status.INTERNAL_SERVER_ERROR.getReasonPhrase());
		appExceptionDto.setPath(uriInfo.getPath());
		appExceptionDto.setTimestamp(ZonedDateTime.now(clock));

		LOG.error(ex.getMessage());

		return Response.status(Status.INTERNAL_SERVER_ERROR.getStatusCode()).entity(appExceptionDto).build();
	}

}