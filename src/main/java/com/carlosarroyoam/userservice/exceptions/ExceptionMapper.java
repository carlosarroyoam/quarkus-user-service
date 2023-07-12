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
	public Response mapException(WebApplicationException x) {
		AppExceptionResponse appExceptionResponse = new AppExceptionResponse();
		appExceptionResponse.setMessage(x.getMessage());
		appExceptionResponse.setCode(x.getResponse().getStatus());
		appExceptionResponse.setStatus(x.getResponse().getStatusInfo().getReasonPhrase());
		appExceptionResponse.setPath(uriInfo.getPath());
		appExceptionResponse.setTimestamp(ZonedDateTime.now(ZoneId.systemDefault()));

		return Response.status(x.getResponse().getStatus()).entity(appExceptionResponse).build();
	}
}