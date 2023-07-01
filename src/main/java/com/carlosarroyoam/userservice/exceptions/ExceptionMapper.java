package com.carlosarroyoam.userservice.exceptions;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import com.carlosarroyoam.userservice.dto.AppExceptionDto;

import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

class ExceptionMappers {

	@Inject
	UriInfo uriInfo;

	@ServerExceptionMapper
	public Response mapException(WebApplicationException x) {
		AppExceptionDto appExceptionDto = new AppExceptionDto();
		appExceptionDto.setMessage(x.getMessage());
		appExceptionDto.setCode(x.getResponse().getStatus());
		appExceptionDto.setStatus(x.getResponse().getStatusInfo().getReasonPhrase());
		appExceptionDto.setPath(uriInfo.getPath());
		appExceptionDto.setTimestamp(ZonedDateTime.now(ZoneId.systemDefault()));

		return Response.status(x.getResponse().getStatus()).entity(appExceptionDto).build();
	}
}