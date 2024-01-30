package com.carlosarroyoam.userservice.resource;

import java.net.URI;
import java.util.List;

import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestResponse;

import com.carlosarroyoam.userservice.dto.CreateUserDto;
import com.carlosarroyoam.userservice.dto.UserDto;
import com.carlosarroyoam.userservice.service.UserService;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

@Path("/api/v1/users")
@ApplicationScoped
@Authenticated
public class UserResource {

	private final UserService userService;

	@Inject
	public UserResource(final UserService userService) {
		this.userService = userService;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("Admin")
	public RestResponse<List<UserDto>> findAll() {
		List<UserDto> users = userService.findAll();
		return RestResponse.ok(users);
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("Admin")
	public RestResponse<UserDto> findById(@RestPath Long id) {
		UserDto userById = userService.findById(id);
		return RestResponse.ok(userById);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("Admin")
	public RestResponse<Object> create(@Valid CreateUserDto createUserRequest) {
		UserDto userDto = userService.create(createUserRequest);
		return RestResponse.created(URI.create("/users/" + userDto.getId()));
	}

	@GET
	@Path("/me")
	@Produces(MediaType.APPLICATION_JSON)
	public RestResponse<UserDto> me(@Context SecurityContext securityContext) {
		String principalName = securityContext.getUserPrincipal().getName();
		UserDto userByUsername = userService.findByUsername(principalName);
		return RestResponse.ok(userByUsername);
	}

}
