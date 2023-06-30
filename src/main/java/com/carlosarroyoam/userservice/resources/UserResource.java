package com.carlosarroyoam.userservice.resources;

import java.net.URI;

import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestResponse;

import com.carlosarroyoam.userservice.model.User;
import com.carlosarroyoam.userservice.services.UserService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@ApplicationScoped
public class UserResource {

	private final UserService userService;

	@Inject
	public UserResource(final UserService userService) {
		super();
		this.userService = userService;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAll() {
		return Response.ok(userService.findAll()).build();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public RestResponse<User> findById(@RestPath Long id) {
		return RestResponse.ok(userService.findById(id));
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Transactional
	public RestResponse<?> create(User user) {
		userService.create(user);
		return RestResponse.created(URI.create("/users/" + user.getId()));
	}
}
