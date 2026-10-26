package com.carlosarroyoam.userservice.auth;

import com.carlosarroyoam.userservice.auth.dto.LoginRequestDto;
import com.carlosarroyoam.userservice.auth.dto.LoginResponseDto;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.RestResponse;

/**
 * Expone el endpoint público de autenticación bajo {@code /api/v1/auth}, delegando la lógica en
 * {@link AuthService}.
 */
@Path("/api/v1/auth")
@ApplicationScoped
@PermitAll
public class AuthResource {
  private final AuthService authService;

  /**
   * Crea el recurso con el servicio de autenticación.
   *
   * @param authService servicio con la lógica de autenticación
   */
  @Inject
  public AuthResource(final AuthService authService) {
    this.authService = authService;
  }

  /**
   * Autentica a un usuario con su nombre de usuario y contraseña.
   *
   * @param requestDto credenciales del usuario
   * @return respuesta 200 con el nombre de usuario y el token de acceso
   */
  @POST
  @Path("/signin")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public RestResponse<LoginResponseDto> auth(@Valid LoginRequestDto requestDto) {
    return RestResponse.ok(authService.auth(requestDto));
  }
}
