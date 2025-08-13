package com.carlosarroyoam.userservice.auth;

import com.carlosarroyoam.userservice.user.entity.Role;
import com.carlosarroyoam.userservice.user.entity.User;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@QuarkusTest
class TokenServiceTest {
  @Inject
  private TokenService tokenService;

  @Test
  @DisplayName("Should return token")
  void shouldReturnToken() {
    Role role = Role.builder().id(1).title("Admin").description("Role for admins users").build();

    User user = User.builder()
        .id(1L)
        .username("carroyom")
        .email("carroyom@mail.com")
        .password("$2a$10$eAksNP3QN8numBgJwshVpOg2ywD5o6YxOW/4WCrk/dZmV77pC6QqC")
        .isActive(Boolean.TRUE)
        .role(role)
        .roleId(role.getId())
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();

    String token = tokenService.generateToken(user);

    assertThat(token, notNullValue());
  }
}
