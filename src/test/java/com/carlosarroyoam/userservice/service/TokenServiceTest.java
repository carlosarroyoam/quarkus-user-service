package com.carlosarroyoam.userservice.service;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.carlosarroyoam.userservice.model.User;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class TokenServiceTest {

	@Inject
	private TokenService tokenService;

	@Test
	@DisplayName("Should return token")
	void shouldReturnToken() {
		User user = new User();
		user.setUsername("carroyom");
		user.setRole("Admin");

		String token = tokenService.generateToken(user);

		assertThat(token, notNullValue());
	}

}
