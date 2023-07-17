package com.carlosarroyoam.userservice.exceptions;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import com.carlosarroyoam.userservice.dto.AppExceptionResponse;

import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

class ExceptionMappers {

	@Inject
	UriInfo uriInfo;

	@ServerExceptionMapper
	public Response mapException(WebApplicationException ex) {
		AppExceptionResponse appExceptionResponse = new AppExceptionResponse();
		appExceptionResponse.setMessage(ex.getMessage());
		appExceptionResponse.setCode(ex.getResponse().getStatus());
		appExceptionResponse.setStatus(ex.getResponse().getStatusInfo().getReasonPhrase());
		appExceptionResponse.setPath(uriInfo.getPath());
		appExceptionResponse.setTimestamp(ZonedDateTime.now(ZoneId.systemDefault()));

		return Response.status(ex.getResponse().getStatus()).entity(appExceptionResponse).build();
	}

}