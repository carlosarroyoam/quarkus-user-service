package com.carlosarroyoam.userservice.services;

import java.time.ZonedDateTime;
import java.util.List;

import org.jboss.logging.Logger;

import com.carlosarroyoam.userservice.constants.AppMessages;
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
			LOG.errorf(AppMessages.USER_ID_NOT_FOUND_EXCEPTION_DETAILED_MESSAGE, id);
			throw new NotFoundException(AppMessages.USER_ID_NOT_FOUND_EXCEPTION_MESSAGE);
		}

		return userById;
	}

	@Transactional
	public User create(User user) {
		User userByUsername = userRepository.findByUsername(user.getUsername());

		if (userByUsername != null) {
			LOG.errorf(AppMessages.USERNAME_ALREADY_EXISTS_EXCEPTION_DETAILED_MESSAGE, user.getUsername());
			throw new BadRequestException(AppMessages.USERNAME_ALREADY_EXISTS_EXCEPTION_MESSAGE);
		}

		User userByMail = userRepository.findByMail(user.getMail());

		if (userByMail != null) {
			LOG.errorf(AppMessages.MAIL_ALREADY_EXISTS_EXCEPTION_DETAILED_MESSAGE, user.getUsername());
			throw new BadRequestException(AppMessages.MAIL_ALREADY_EXISTS_EXCEPTION_MESSAGE);
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
			LOG.errorf(AppMessages.USER_USERNAME_NOT_FOUND_EXCEPTION_DETAILED_MESSAGE, username);
			throw new NotFoundException(AppMessages.USER_USERNAME_NOT_FOUND_EXCEPTION_MESSAGE);
		}

		return userByUsername;
	}

}
