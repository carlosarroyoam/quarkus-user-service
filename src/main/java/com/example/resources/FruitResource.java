package com.example.resources;

import java.net.URI;

import org.jboss.logging.Logger;

import com.example.model.Fruit;
import com.example.services.FruitService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/fruits")
@ApplicationScoped
public class FruitResource {

	private static final Logger LOG = Logger.getLogger(FruitResource.class);

	@Inject
	FruitService fruitService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		LOG.info("Getting all fruits");

		return Response.ok(fruitService.getAll()).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response add(final Fruit fruit) {
		LOG.info("Adding fruit: " + fruit.getName());
		fruitService.add(fruit);
		return Response.created(URI.create("/fruits/" + fruit.getName())).build();
	}

}