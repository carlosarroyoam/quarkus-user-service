package com.carlosarroyoam.userservice.resource;

import org.jboss.resteasy.reactive.RestResponse;

import com.carlosarroyoam.userservice.dto.LoginRequest;
import com.carlosarroyoam.userservice.dto.LoginResponse;
import com.carlosarroyoam.userservice.service.AuthService;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/v1/auth")
@ApplicationScoped
@PermitAll
public class AuthResource {

	private final AuthService authService;

	@Inject
	public AuthResource(final AuthService authService) {
		this.authService = authService;
	}

	@POST
	@Path("/signin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public RestResponse<LoginResponse> auth(@Valid LoginRequest loginRequest) {
		return RestResponse.ok(authService.auth(loginRequest));
	}

}
