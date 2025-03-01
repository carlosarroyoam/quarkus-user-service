package com.carlosarroyoam.userservice.config;

import io.quarkus.qute.i18n.Message;
import io.quarkus.qute.i18n.MessageBundle;

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
