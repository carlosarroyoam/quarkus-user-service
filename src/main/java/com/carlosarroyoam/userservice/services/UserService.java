package com.carlosarroyoam.userservice.services;

import java.time.ZonedDateTime;
import java.util.List;

import org.jboss.logging.Logger;

import com.carlosarroyoam.userservice.constants.AppMessages;
import com.carlosarroyoam.userservice.dto.CreateUserRequest;
import com.carlosarroyoam.userservice.dto.UserResponse;
import com.carlosarroyoam.userservice.mappers.UserMapper;
import com.carlosarroyoam.userservice.model.User;
import com.carlosarroyoam.userservice.repositories.UserRepository;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UserService {

	private static final Logger LOG = Logger.getLogger(UserService.class);
	private final UserRepository userRepository;
	private final UserMapper mapper;

	@Inject
	public UserService(final UserRepository userRepository, final UserMapper mapper) {
		super();
		this.userRepository = userRepository;
		this.mapper = mapper;
	}

	public List<UserResponse> findAll() {
		return mapper.toDtos(userRepository.listAll());
	}

	public UserResponse findById(Long id) {
		User userById = userRepository.findById(id);

		if (userById == null) {
			LOG.errorf(AppMessages.USER_ID_NOT_FOUND_EXCEPTION_DETAILED_MESSAGE, id);
			throw new NotFoundException(AppMessages.USER_ID_NOT_FOUND_EXCEPTION_MESSAGE);
		}

		return mapper.toDto(userById);
	}

	public UserResponse findByUsername(String username) {
		User userByUsername = userRepository.findByUsername(username);

		if (userByUsername == null) {
			LOG.errorf(AppMessages.USER_USERNAME_NOT_FOUND_EXCEPTION_DETAILED_MESSAGE, username);
			throw new NotFoundException(AppMessages.USER_USERNAME_NOT_FOUND_EXCEPTION_MESSAGE);
		}

		return mapper.toDto(userByUsername);
	}

	public UserResponse create(CreateUserRequest createUserRequest) {
		User userByUsername = userRepository.findByUsername(createUserRequest.getUsername());

		if (userByUsername != null) {
			LOG.errorf(AppMessages.USERNAME_ALREADY_EXISTS_EXCEPTION_DETAILED_MESSAGE, createUserRequest.getUsername());
			throw new BadRequestException(AppMessages.USERNAME_ALREADY_EXISTS_EXCEPTION_MESSAGE);
		}

		User userByMail = userRepository.findByMail(createUserRequest.getMail());

		if (userByMail != null) {
			LOG.errorf(AppMessages.MAIL_ALREADY_EXISTS_EXCEPTION_DETAILED_MESSAGE, createUserRequest.getUsername());
			throw new BadRequestException(AppMessages.MAIL_ALREADY_EXISTS_EXCEPTION_MESSAGE);
		}

		User user = mapper.createRequestToEntity(createUserRequest);

		user.setPassword(BcryptUtil.bcryptHash(user.getPassword()));
		user.setIsActive(Boolean.FALSE);
		user.setCreatedAt(ZonedDateTime.now());
		user.setUpdatedAt(ZonedDateTime.now());

		userRepository.persist(user);

		return mapper.toDto(user);
	}

}
