package com.carlosarroyoam.userservice.service;

import org.jboss.logging.Logger;

import com.carlosarroyoam.userservice.config.AppMessages;
import com.carlosarroyoam.userservice.dto.LoginRequest;
import com.carlosarroyoam.userservice.dto.LoginResponse;
import com.carlosarroyoam.userservice.entity.User;
import com.carlosarroyoam.userservice.repository.UserRepository;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.AuthenticationFailedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthService {
	private static final Logger LOG = Logger.getLogger(AuthService.class);
	private final UserRepository userRepository;
	private final TokenService tokenService;
	private final AppMessages messages;

	@Inject
	public AuthService(final UserRepository userRepository, final TokenService tokenService, AppMessages messages) {
		this.userRepository = userRepository;
		this.tokenService = tokenService;
		this.messages = messages;
	}

	public LoginResponse auth(LoginRequest loginRequest) {
		User userByUsername = userRepository.findByUsernameOptional(loginRequest.getUsername()).orElseThrow(() -> {
			LOG.warn(messages.userAccountNotFoundDetailed(loginRequest.getUsername()));
			return new AuthenticationFailedException(messages.userAccountNotFound());
		});

		if (Boolean.FALSE.equals(userByUsername.getIsActive())) {
			LOG.warn(messages.userAccountNotActiveDetailed(loginRequest.getUsername()));
			throw new AuthenticationFailedException(messages.userAccountNotActive());
		}

		if (!BcryptUtil.matches(loginRequest.getPassword(), userByUsername.getPassword())) {
			LOG.warn(messages.unauthorizedCredentialsDetailed(loginRequest.getUsername()));
			throw new AuthenticationFailedException(messages.unauthorizedCredentials());
		}

		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setUsername(userByUsername.getUsername());
		loginResponse.setAccessToken(tokenService.generateToken(userByUsername));
		return loginResponse;
	}
}
