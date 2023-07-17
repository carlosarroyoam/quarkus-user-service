package com.carlosarroyoam.userservice.services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.carlosarroyoam.userservice.constants.AppMessages;
import com.carlosarroyoam.userservice.dto.LoginRequest;
import com.carlosarroyoam.userservice.dto.LoginResponse;
import com.carlosarroyoam.userservice.model.User;
import com.carlosarroyoam.userservice.repositories.UserRepository;

import io.quarkus.security.AuthenticationFailedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
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
	void testAuthsUserWithCorrectCredentials() {
		Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(getUser(true));
		Mockito.when(tokenService.generateToken(Mockito.any(User.class))).thenReturn(getToken());

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("carroyom");
		loginRequest.setPassword("secret");

		LoginResponse loginResponse = authService.auth(loginRequest);

		assertThat(loginResponse.getUsername(), equalTo("carroyom"));
		assertThat(loginResponse.getAccessToken(), equalTo("adQssw5c"));
	}

	@Test
	void testAuthFailsWithNonExistingUser() {
		Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(null);

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("carroyom");
		loginRequest.setPassword("secret");

		Throwable ex = assertThrows(AuthenticationFailedException.class, () -> authService.auth(loginRequest));

		assertThat(ex.getMessage(), equalTo(AppMessages.USER_ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE));
		assertThat(ex, instanceOf(AuthenticationFailedException.class));
	}

	@Test
	void testAuthFailsWithWrongCredentials() {
		Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(getUser(true));

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("carroyom");
		loginRequest.setPassword("wrong-pass");

		Throwable ex = assertThrows(AuthenticationFailedException.class, () -> authService.auth(loginRequest));

		assertThat(ex.getMessage(), equalTo(AppMessages.UNAUTHORIZED_CREDENTIALS_EXCEPTION_MESSAGE));
		assertThat(ex, instanceOf(AuthenticationFailedException.class));
	}

	@Test
	void testAuthFailsWithInactiveUser() {
		Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(getUser(false));

		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("carroyom");
		loginRequest.setPassword("secret");

		Throwable ex = assertThrows(AuthenticationFailedException.class, () -> authService.auth(loginRequest));

		assertThat(ex.getMessage(), equalTo(AppMessages.USER_ACCOUNT_NOT_ACTIVE_EXCEPTION_MESSAGE));
		assertThat(ex, instanceOf(AuthenticationFailedException.class));
	}

	private String getToken() {
		return "adQssw5c";
	}

	private User getUser(Boolean isActive) {
		User user = new User();
		user.setUsername("carroyom");
		user.setPassword("$2a$10$eAksNP3QN8numBgJwshVpOg2ywD5o6YxOW/4WCrk/dZmV77pC6QqC");
		user.setIsActive(isActive);
		return user;
	}
}
