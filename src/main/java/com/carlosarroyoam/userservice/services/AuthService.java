package com.carlosarroyoam.userservice.services;

import org.jboss.logging.Logger;

import com.carlosarroyoam.userservice.model.User;
import com.carlosarroyoam.userservice.repositories.UserRepository;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.security.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

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

	public String auth(User user) {
		User userByUsername = userRepository.findByUsername(user.getUsername());

		if (userByUsername == null) {
			LOG.error("User with username:" + user.getUsername() + " not found");
			throw new NotFoundException("User with username:" + user.getUsername() + " not found");
		}
		
		if(!BcryptUtil.matches(user.getPassword(), userByUsername.getPassword())) {
			LOG.error("Unauthorized credentials for user with username:" + user.getUsername());
			throw new UnauthorizedException("Unauthorized credentials");
		}

		return tokenService.generateToken(userByUsername);
	}

}
