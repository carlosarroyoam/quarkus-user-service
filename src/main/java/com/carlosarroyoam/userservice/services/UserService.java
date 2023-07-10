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
			LOG.error("User with id:" + id + " not found");
			throw new NotFoundException("User with id:" + id + " not found");
		}

		return userById;
	}

	@Transactional
	public User create(User user) {
		User userByUsername = userRepository.findByUsername(user.getUsername());

		if (userByUsername != null) {
			LOG.error("User with username:" + userByUsername.getUsername() + " already exists");
			throw new BadRequestException("User with username:" + userByUsername.getUsername() + " already exists");
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
			LOG.error("User with username:" + username + " not found");
			throw new NotFoundException("User with username:" + username + " not found");
		}

		return userByUsername;
	}

}
