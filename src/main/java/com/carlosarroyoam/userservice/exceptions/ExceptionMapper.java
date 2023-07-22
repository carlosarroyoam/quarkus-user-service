package com.carlosarroyoam.userservice.exceptions;

import java.time.Clock;
import java.time.ZonedDateTime;

import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import com.carlosarroyoam.userservice.dto.AppExceptionDto;

import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

class ExceptionMappers {

	private final UriInfo uriInfo;

	private final Clock clock;

	@Inject
	public ExceptionMappers(UriInfo uriInfo, Clock clock) {
		super();
		this.uriInfo = uriInfo;
		this.clock = clock;
	}

	@ServerExceptionMapper
	public Response mapException(WebApplicationException ex) {
		AppExceptionDto appExceptionDto = new AppExceptionDto();
		appExceptionDto.setMessage(ex.getMessage());
		appExceptionDto.setCode(ex.getResponse().getStatus());
		appExceptionDto.setStatus(ex.getResponse().getStatusInfo().getReasonPhrase());
		appExceptionDto.setPath(uriInfo.getPath());
		appExceptionDto.setTimestamp(ZonedDateTime.now(clock));

		return Response.status(ex.getResponse().getStatus()).entity(appExceptionDto).build();
	}

}