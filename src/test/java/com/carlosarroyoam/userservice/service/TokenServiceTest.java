package com.carlosarroyoam.userservice.service;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.carlosarroyoam.userservice.entity.Role;
import com.carlosarroyoam.userservice.entity.User;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@QuarkusTest
class TokenServiceTest {
  @Inject
  private TokenService tokenService;

  @Test
  @DisplayName("Should return token")
  void shouldReturnToken() {
    Role role = new Role();
    role.setId(1);
    role.setTitle("App//Admin");

    User user = new User();
    user.setUsername("carroyom");
    user.setRole(role);

    String token = tokenService.generateToken(user);

    assertThat(token, notNullValue());
  }
}
