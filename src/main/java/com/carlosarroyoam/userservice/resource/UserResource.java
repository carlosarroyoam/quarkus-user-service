package com.carlosarroyoam.userservice.resource;

import com.carlosarroyoam.userservice.dto.ChangePasswordRequest;
import com.carlosarroyoam.userservice.dto.CreateUserRequest;
import com.carlosarroyoam.userservice.dto.UpdateUserRequest;
import com.carlosarroyoam.userservice.dto.UserResponse;
import com.carlosarroyoam.userservice.service.UserService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import java.net.URI;
import java.util.List;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestResponse;

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
  public RestResponse<List<UserResponse>> findAll() {
    return RestResponse.ok(userService.findAll());
  }

  @GET
  @Path("/{userId}")
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed("Admin")
  public RestResponse<UserResponse> findById(@RestPath("userId") Long userId) {
    return RestResponse.ok(userService.findById(userId));
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @RolesAllowed("Admin")
  public RestResponse<Void> create(@Valid CreateUserRequest createUserRequest) {
    UserResponse userResponse = userService.create(createUserRequest);
    return RestResponse.created(URI.create("/users/" + userResponse.getId()));
  }

  @PUT
  @Path("/{userId}")
  @Consumes(MediaType.APPLICATION_JSON)
  @RolesAllowed("Admin")
  public RestResponse<Void> update(@RestPath("userId") Long userId,
      @Valid UpdateUserRequest updateUserRequest) {
    userService.update(userId, updateUserRequest);
    return RestResponse.noContent();
  }

  @POST
  @Path("/{userId}/change-password")
  @Consumes(MediaType.APPLICATION_JSON)
  public RestResponse<Void> changePassword(@Context SecurityContext securityContext,
      @Valid ChangePasswordRequest changePasswordRequest) {
    UserResponse userByUsername = userService
        .findByUsername(securityContext.getUserPrincipal().getName());
    userService.changePassword(userByUsername.getId(), changePasswordRequest);
    return RestResponse.noContent();
  }

  @DELETE
  @Path("/{userId}")
  @RolesAllowed("Admin")
  public RestResponse<Void> deleteById(@RestPath("userId") Long userId) {
    userService.deleteById(userId);
    return RestResponse.noContent();
  }

  @GET
  @Path("/self")
  @Produces(MediaType.APPLICATION_JSON)
  public RestResponse<UserResponse> self(@Context SecurityContext securityContext) {
    return RestResponse
        .ok(userService.findByUsername(securityContext.getUserPrincipal().getName()));
  }
}
