package com.example;

import java.util.ArrayList;
import java.util.List;

import com.example.domain.User;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/users")
@ApplicationScoped
public class UserResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response hello() {
		List<User> users = new ArrayList<>();
		users.add(new User("Carlos Alberto Arroyo Martínez", "carroyom"));
		users.add(new User("Cathy Stefania Guido Rojas", "cguidor"));

		return Response.ok(users).build();
	}

}
