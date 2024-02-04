package com.carlosarroyoam.userservice.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.carlosarroyoam.userservice.constant.AppMessages;
import com.carlosarroyoam.userservice.dto.LoginRequest;
import com.carlosarroyoam.userservice.dto.LoginResponse;
import com.carlosarroyoam.userservice.model.User;
import com.carlosarroyoam.userservice.repository.UserRepository;

import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
class AuthServiceTest {

	@Inject
	private AuthService authService;

	@InjectMock
	private UserRepository userRepository;

	@InjectMock
	private TokenService tokenService;

	@Test
	@DisplayName("Should return LoginResponse when attempt to auth a user with valid credentials")
	void testAuthsUserWithCorrectCredentials() {
		Mockito.when(userRepository.findByUsernameOptional(Mockito.anyString())).thenReturn(createTestUser(true));
		Mockito.when(tokenService.generateToken(Mockito.any(User.class))).thenReturn(createTestToken());

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("carroyom");
		loginRequest.setPassword("secret");

		LoginResponse loginResponse = authService.auth(loginRequest);

		assertThat(loginResponse.getUsername(), equalTo("carroyom"));
		assertThat(loginResponse.getAccessToken(), equalTo("eAksNP3QN8numBgJwshVpOg2ywD5o6YxOW"));
	}

	@Test
	@DisplayName("Should throw exception when attempt to auth with non existing user")
	void testAuthFailsWithNonExistingUser() {
		Mockito.when(userRepository.findByUsernameOptional(Mockito.anyString())).thenReturn(Optional.empty());

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("carroyom");
		loginRequest.setPassword("secret");

		Throwable ex = assertThrows(AuthenticationFailedException.class, () -> authService.auth(loginRequest));

		assertThat(ex.getMessage(), equalTo(AppMessages.USER_ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE));
		assertThat(ex, instanceOf(AuthenticationFailedException.class));
	}

	@Test
	@DisplayName("Should throw exception when attempt to auth with invalid credentials")
	void testAuthFailsWithWrongCredentials() {
		Mockito.when(userRepository.findByUsernameOptional(Mockito.anyString())).thenReturn(createTestUser(true));

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("carroyom");
		loginRequest.setPassword("wrong-pass");

		Throwable ex = assertThrows(AuthenticationFailedException.class, () -> authService.auth(loginRequest));

		assertThat(ex.getMessage(), equalTo(AppMessages.UNAUTHORIZED_CREDENTIALS_EXCEPTION_MESSAGE));
		assertThat(ex, instanceOf(AuthenticationFailedException.class));
	}

	@Test
	@DisplayName("Should throw exception when attempt to auth an inactive user")
	void testAuthFailsWithInactiveUser() {
		Mockito.when(userRepository.findByUsernameOptional(Mockito.anyString())).thenReturn(createTestUser(false));

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("carroyom");
		loginRequest.setPassword("secret");

		Throwable ex = assertThrows(AuthenticationFailedException.class, () -> authService.auth(loginRequest));

		assertThat(ex.getMessage(), equalTo(AppMessages.USER_ACCOUNT_NOT_ACTIVE_EXCEPTION_MESSAGE));
		assertThat(ex, instanceOf(AuthenticationFailedException.class));
	}

	private String createTestToken() {
		return "eAksNP3QN8numBgJwshVpOg2ywD5o6YxOW";
	}

	private Optional<User> createTestUser(Boolean isActive) {
		User user = new User();
		user.setUsername("carroyom");
		user.setPassword("$2a$10$eAksNP3QN8numBgJwshVpOg2ywD5o6YxOW/4WCrk/dZmV77pC6QqC");
		user.setIsActive(isActive);

		return Optional.of(user);
	}
}
