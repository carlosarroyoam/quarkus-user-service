package com.carlosarroyoam.userservice.resources;

import org.jboss.resteasy.reactive.RestResponse;

import com.carlosarroyoam.userservice.model.User;
import com.carlosarroyoam.userservice.services.AuthService;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

@Path("/auth")
@ApplicationScoped
public class AuthResource {

	private final AuthService authService;

	@Inject
	public AuthResource(final AuthService authService) {
		super();
		this.authService = authService;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@PermitAll
	public RestResponse<String> auth(User user) {
		String token =  authService.auth(user);
		return RestResponse.ok(token);
	}
}
