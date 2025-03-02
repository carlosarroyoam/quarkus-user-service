package com.carlosarroyoam.userservice.service;

import com.carlosarroyoam.userservice.config.AppMessages;
import com.carlosarroyoam.userservice.dto.LoginRequestDto;
import com.carlosarroyoam.userservice.dto.LoginResponseDto;
import com.carlosarroyoam.userservice.entity.User;
import com.carlosarroyoam.userservice.repository.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.AuthenticationFailedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

@ApplicationScoped
public class AuthService {
  private static final Logger LOG = Logger.getLogger(AuthService.class);
  private final UserRepository userRepository;
  private final TokenService tokenService;
  private final AppMessages messages;

  @Inject
  public AuthService(final UserRepository userRepository, final TokenService tokenService,
      AppMessages messages) {
    this.userRepository = userRepository;
    this.tokenService = tokenService;
    this.messages = messages;
  }

  public LoginResponseDto auth(LoginRequestDto requestDto) {
    User userByUsername = userRepository.findByUsernameOptional(requestDto.getUsername())
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
