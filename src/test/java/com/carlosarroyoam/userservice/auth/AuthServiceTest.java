package com.carlosarroyoam.userservice.auth;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.carlosarroyoam.userservice.auth.dto.LoginRequestDto;
import com.carlosarroyoam.userservice.auth.dto.LoginResponseDto;
import com.carlosarroyoam.userservice.core.config.AppMessages;
import com.carlosarroyoam.userservice.user.UserRepository;
import com.carlosarroyoam.userservice.user.entity.Role;
import com.carlosarroyoam.userservice.user.entity.User;
import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

@QuarkusTest
class AuthServiceTest {
  @Inject
  private AuthService authService;

  @InjectMock
  private UserRepository userRepository;

  @InjectMock
  private TokenService tokenService;

  @Inject
  private AppMessages messages;

  @Test
  @DisplayName("Should return LoginResponse when attempt to auth a user with valid credentials")
  void shouldReturnLoginResponseWhenAuthWithValidCredentials() {
    LoginRequestDto loginRequestDto = LoginRequestDto.builder()
        .username("carroyom")
        .password("secret")
        .build();

    Optional<User> user = createUser(true);

    String jwt = createToken();

    Mockito.when(userRepository.findByUsernameOptional(ArgumentMatchers.anyString()))
        .thenReturn(user);

    Mockito.when(tokenService.generateToken(ArgumentMatchers.any(User.class))).thenReturn(jwt);

    LoginResponseDto loginResponseDto = authService.auth(loginRequestDto);

    assertThat(loginResponseDto.getUsername(), equalTo(user.get().getUsername()));
    assertThat(loginResponseDto.getAccessToken(), equalTo(jwt));
  }

  @Test
  @DisplayName("Should throw exception when attempt to auth with non existing user")
  void shouldThrowExceptionWhenAuthWithNonExistingUser() {
    LoginRequestDto loginRequestDto = LoginRequestDto.builder()
        .username("carroyom")
        .password("secret")
        .build();

    Mockito.when(userRepository.findByUsernameOptional(ArgumentMatchers.anyString()))
        .thenReturn(Optional.empty());

    Throwable ex = assertThrows(AuthenticationFailedException.class,
        () -> authService.auth(loginRequestDto));

    assertThat(ex.getMessage(), equalTo(messages.userAccountNotFound()));
    assertThat(ex, instanceOf(AuthenticationFailedException.class));
  }

  @Test
  @DisplayName("Should throw exception when attempt to auth with invalid credentials")
  void shouldThrowExceptionWhenAuthWithInvalidCredentials() {
    LoginRequestDto loginRequestDto = LoginRequestDto.builder()
        .username("carroyom")
        .password("wrong-pass")
        .build();

    Mockito.when(userRepository.findByUsernameOptional(ArgumentMatchers.anyString()))
        .thenReturn(createUser(true));

    Throwable ex = assertThrows(AuthenticationFailedException.class,
        () -> authService.auth(loginRequestDto));

    assertThat(ex.getMessage(), equalTo(messages.unauthorizedCredentials()));
    assertThat(ex, instanceOf(AuthenticationFailedException.class));
  }

  @Test
  @DisplayName("Should throw exception when attempt to auth an inactive user")
  void shouldThrowExceptionWhenAuthWithInactiveUser() {
    LoginRequestDto loginRequestDto = LoginRequestDto.builder()
        .username("carroyom")
        .password("secret")
        .build();

    Mockito.when(userRepository.findByUsernameOptional(ArgumentMatchers.anyString()))
        .thenReturn(createUser(false));

    Throwable ex = assertThrows(AuthenticationFailedException.class,
        () -> authService.auth(loginRequestDto));

    assertThat(ex.getMessage(), equalTo(messages.userAccountNotActive()));
    assertThat(ex, instanceOf(AuthenticationFailedException.class));
  }

  private String createToken() {
    return "eAksNP3QN8numBgJwshVpOg2ywD5o6YxOW";
  }

  private Optional<User> createUser(Boolean isActive) {
    Role role = Role.builder().id(1).title("Admin").description("Role for admins users").build();

    User user = User.builder()
        .id(1L)
        .username("carroyom")
        .email("carroyom@mail.com")
        .password("$2a$10$eAksNP3QN8numBgJwshVpOg2ywD5o6YxOW/4WCrk/dZmV77pC6QqC")
        .isActive(isActive)
        .role(role)
        .roleId(role.getId())
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();

    return Optional.of(user);
  }
}
