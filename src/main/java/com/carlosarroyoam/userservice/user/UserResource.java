package com.carlosarroyoam.userservice.user;

import com.carlosarroyoam.userservice.user.dto.ChangePasswordRequestDto;
import com.carlosarroyoam.userservice.user.dto.CreateUserRequestDto;
import com.carlosarroyoam.userservice.user.dto.UpdateUserRequestDto;
import com.carlosarroyoam.userservice.user.dto.UserDto;
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

/**
 * Expone los endpoints REST de gestión de usuarios bajo {@code /api/v1/users}, delegando la lógica
 * de negocio en {@link UserService}. Todos los endpoints requieren autenticación.
 */
@Path("/api/v1/users")
@ApplicationScoped
@Authenticated
public class UserResource {
  private final UserService userService;

  /**
   * Crea el recurso con el servicio de usuarios.
   *
   * @param userService servicio con la lógica de negocio de usuarios
   */
  @Inject
  public UserResource(final UserService userService) {
    this.userService = userService;
  }

  /**
   * Lista todos los usuarios. Requiere rol {@code Admin}.
   *
   * @return respuesta 200 con la lista de usuarios
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed("Admin")
  public RestResponse<List<UserDto>> findAll() {
    return RestResponse.ok(userService.findAll());
  }

  /**
   * Busca un usuario por su identificador. Requiere rol {@code Admin}.
   *
   * @param userId identificador del usuario
   * @return respuesta 200 con el usuario encontrado
   */
  @GET
  @Path("/{userId}")
  @Produces(MediaType.APPLICATION_JSON)
  @RolesAllowed("Admin")
  public RestResponse<UserDto> findById(@RestPath("userId") Long userId) {
    return RestResponse.ok(userService.findById(userId));
  }

  /**
   * Crea un nuevo usuario. Requiere rol {@code Admin}.
   *
   * @param requestDto datos del usuario a crear
   * @return respuesta 201 con la cabecera {@code Location} del usuario creado
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @RolesAllowed("Admin")
  public RestResponse<Void> create(@Valid CreateUserRequestDto requestDto) {
    UserDto userDto = userService.create(requestDto);
    return RestResponse.created(URI.create("/users/" + userDto.getId()));
  }

  /**
   * Actualiza el nombre y la edad de un usuario. Requiere rol {@code Admin}.
   *
   * @param userId identificador del usuario
   * @param requestDto nuevos datos del usuario
   * @return respuesta 204 sin contenido
   */
  @PUT
  @Path("/{userId}")
  @Consumes(MediaType.APPLICATION_JSON)
  @RolesAllowed("Admin")
  public RestResponse<Void> update(
      @RestPath("userId") Long userId, @Valid UpdateUserRequestDto requestDto) {
    userService.update(userId, requestDto);
    return RestResponse.noContent();
  }

  /**
   * Cambia la contraseña del usuario autenticado. El identificador de la ruta no se utiliza: el
   * usuario se obtiene del {@link SecurityContext}.
   *
   * @param securityContext contexto de seguridad de la petición
   * @param requestDto contraseña actual, contraseña nueva y su confirmación
   * @return respuesta 204 sin contenido
   */
  @POST
  @Path("/{userId}/change-password")
  @Consumes(MediaType.APPLICATION_JSON)
  public RestResponse<Void> changePassword(
      @Context SecurityContext securityContext, @Valid ChangePasswordRequestDto requestDto) {
    UserDto userByUsername =
        userService.findByUsername(securityContext.getUserPrincipal().getName());
    userService.changePassword(userByUsername.getId(), requestDto);
    return RestResponse.noContent();
  }

  /**
   * Desactiva un usuario mediante baja lógica. Requiere rol {@code Admin}.
   *
   * @param userId identificador del usuario
   * @return respuesta 204 sin contenido
   */
  @DELETE
  @Path("/{userId}")
  @RolesAllowed("Admin")
  public RestResponse<Void> deleteById(@RestPath("userId") Long userId) {
    userService.deleteById(userId);
    return RestResponse.noContent();
  }

  /**
   * Obtiene el usuario autenticado.
   *
   * @param securityContext contexto de seguridad de la petición
   * @return respuesta 200 con el usuario autenticado
   */
  @GET
  @Path("/self")
  @Produces(MediaType.APPLICATION_JSON)
  public RestResponse<UserDto> self(@Context SecurityContext securityContext) {
    UserDto userDto = userService.findByUsername(securityContext.getUserPrincipal().getName());
    return RestResponse.ok(userDto);
  }
}
