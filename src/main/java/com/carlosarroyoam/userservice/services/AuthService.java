package com.carlosarroyoam.userservice.services;

import org.jboss.logging.Logger;

import com.carlosarroyoam.userservice.dto.LoginRequestDto;
import com.carlosarroyoam.userservice.dto.LoginResponseDto;
import com.carlosarroyoam.userservice.model.User;
import com.carlosarroyoam.userservice.repositories.UserRepository;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.AuthenticationFailedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthService {

	private static final Logger LOG = Logger.getLogger(AuthService.class);
	private final UserRepository userRepository;
	private final TokenService tokenService;

	@Inject
	public AuthService(UserRepository userRepository, TokenService tokenService) {
		super();
		this.userRepository = userRepository;
		this.tokenService = tokenService;
	}

	public LoginResponseDto auth(LoginRequestDto loginRequest) {
		User userByUsername = userRepository.findByUsername(loginRequest.getUsername());

		if (userByUsername == null) {
			LOG.errorf("User with username: %s not found", loginRequest.getUsername());
			throw new AuthenticationFailedException();
		}

		if (userByUsername.getIsActive().equals(Boolean.FALSE)) {
			LOG.errorf("User with username: %s is not active", loginRequest.getUsername());
			throw new AuthenticationFailedException();
		}

		if (!BcryptUtil.matches(loginRequest.getPassword(), userByUsername.getPassword())) {
			LOG.errorf("Unauthorized credentials for user with username: %s", loginRequest.getUsername());
			throw new AuthenticationFailedException();
		}

		String token = tokenService.generateToken(userByUsername);

		LoginResponseDto loginResponse = new LoginResponseDto();
		loginResponse.setAccessToken(token);
		loginResponse.setUsername(userByUsername.getUsername());

		return loginResponse;
	}

}
