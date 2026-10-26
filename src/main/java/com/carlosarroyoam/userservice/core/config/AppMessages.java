package com.carlosarroyoam.userservice.core.config;

import io.quarkus.qute.i18n.Message;
import io.quarkus.qute.i18n.MessageBundle;

/**
 * Mensajes localizables de la aplicación, definidos en {@code messages/msg.properties}.
 *
 * <p>Los mensajes con sufijo {@code Detailed} incluyen datos del contexto y se escriben en el log;
 * sus variantes genéricas son las que se devuelven al cliente en las excepciones, de modo que la
 * respuesta no incluya esos datos.
 */
@MessageBundle
public interface AppMessages {
  @Message
  String userAccountNotFound();

  @Message
  String userAccountNotFoundDetailed(String username);

  @Message
  String userAccountNotActive();

  @Message
  String userAccountNotActiveDetailed(String username);

  @Message
  String unauthorizedCredentials();

  @Message
  String unauthorizedCredentialsDetailed(String username);

  @Message
  String userNotFound();

  @Message
  String userWithIdNotFound(Long userId);

  @Message
  String userWithUsernameNotFound(String username);

  @Message
  String passwordsDoesntMatch();

  @Message
  String emailAlreadyTaken();

  @Message
  String emailAlreadyTakenDetailed(String email);

  @Message
  String usernameAlreadyTaken();

  @Message
  String usernameAlreadyTakenDetailed(String username);

  @Message
  String roleNotFound();
}
