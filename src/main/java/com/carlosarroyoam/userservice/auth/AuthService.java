package com.carlosarroyoam.userservice.auth;

import com.carlosarroyoam.userservice.auth.dto.LoginRequestDto;
import com.carlosarroyoam.userservice.auth.dto.LoginResponseDto;
import com.carlosarroyoam.userservice.core.config.AppMessages;
import com.carlosarroyoam.userservice.user.UserRepository;
import com.carlosarroyoam.userservice.user.entity.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.AuthenticationFailedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

/**
 * Contiene la lógica de autenticación de {@link User}: verificación de la cuenta y de las
 * credenciales, y emisión del token de acceso mediante {@link TokenService}.
 */
@ApplicationScoped
public class AuthService {
  private static final Logger LOG = Logger.getLogger(AuthService.class);
  private final UserRepository userRepository;
  private final TokenService tokenService;
  private final AppMessages messages;

  /**
   * Crea el servicio con sus dependencias.
   *
   * @param userRepository repositorio de usuarios
   * @param tokenService servicio que genera los tokens de acceso
   * @param messages mensajes localizados de la aplicación
   */
  @Inject
  public AuthService(
      final UserRepository userRepository, final TokenService tokenService, AppMessages messages) {
    this.userRepository = userRepository;
    this.tokenService = tokenService;
    this.messages = messages;
  }

  /**
   * Autentica a un usuario y genera su token de acceso. La cuenta debe existir y estar activa, y la
   * contraseña debe coincidir con el hash almacenado.
   *
   * @param requestDto credenciales del usuario
   * @return la respuesta con el nombre de usuario y el token de acceso
   * @throws AuthenticationFailedException si la cuenta no existe, está inactiva o las credenciales
   *     son incorrectas
   */
  public LoginResponseDto auth(LoginRequestDto requestDto) {
    User userByUsername =
        userRepository
            .findByUsernameOptional(requestDto.getUsername())
            .orElseThrow(() -> {
              LOG.warn(messages.userAccountNotFoundDetailed(requestDto.getUsername()));
              return new AuthenticationFailedException(messages.userAccountNotFound());
            });

    if (Boolean.FALSE.equals(userByUsername.getIsActive())) {
      LOG.warn(messages.userAccountNotActiveDetailed(requestDto.getUsername()));
      throw new AuthenticationFailedException(messages.userAccountNotActive());
    }

    if (!BcryptUtil.matches(requestDto.getPassword(), userByUsername.getPassword())) {
      LOG.warn(messages.unauthorizedCredentialsDetailed(requestDto.getUsername()));
      throw new AuthenticationFailedException(messages.unauthorizedCredentials());
    }

    return LoginResponseDto.builder()
        .username(userByUsername.getUsername())
        .accessToken(tokenService.generateToken(userByUsername))
        .build();
  }
}
