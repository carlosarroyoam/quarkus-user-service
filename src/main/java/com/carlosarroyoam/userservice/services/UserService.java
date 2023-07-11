package com.carlosarroyoam.userservice.services;

import java.time.ZonedDateTime;
import java.util.List;

import org.jboss.logging.Logger;

import com.carlosarroyoam.userservice.model.User;
import com.carlosarroyoam.userservice.repositories.UserRepository;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UserService {

	private static final Logger LOG = Logger.getLogger(UserService.class);
	private final UserRepository userRepository;

	@Inject
	public UserService(final UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	public List<User> findAll() {
		return userRepository.listAll();
	}

	public User findById(Long id) {
		User userById = userRepository.findById(id);

		if (userById == null) {
			LOG.errorf("User with id: %d not found", id);
			throw new NotFoundException(String.format("User with id: %d not found", id));
		}

		return userById;
	}

	@Transactional
	public User create(User user) {
		User userByUsername = userRepository.findByUsername(user.getUsername());

		if (userByUsername != null) {
			LOG.errorf("User with username: %s already exists", user.getUsername());
			throw new BadRequestException(String.format("User with username: %s already exists", user.getUsername()));
		}

		user.setPassword(BcryptUtil.bcryptHash(user.getPassword()));
		user.setIsActive(Boolean.FALSE);
		user.setCreatedAt(ZonedDateTime.now());
		user.setUpdatedAt(ZonedDateTime.now());

		userRepository.persist(user);

		return user;
	}

	public User findByUsername(String username) {
		User userByUsername = userRepository.findByUsername(username);

		if (userByUsername == null) {
			LOG.errorf("User with username: %s not found", username);
			throw new NotFoundException(String.format("User with username: %s not found", username));
		}

		return userByUsername;
	}

}
