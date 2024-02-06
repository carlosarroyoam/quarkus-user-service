package com.carlosarroyoam.userservice.service;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;

import org.jboss.logging.Logger;

import com.carlosarroyoam.userservice.config.AppMessages;
import com.carlosarroyoam.userservice.dto.ChangePasswordRequest;
import com.carlosarroyoam.userservice.dto.CreateUserRequest;
import com.carlosarroyoam.userservice.dto.UpdateUserRequest;
import com.carlosarroyoam.userservice.dto.UserResponse;
import com.carlosarroyoam.userservice.mapper.UserMapper;
import com.carlosarroyoam.userservice.model.User;
import com.carlosarroyoam.userservice.repository.UserRepository;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
@Transactional
public class UserService {

	private static final Logger LOG = Logger.getLogger(UserService.class);
	private final UserRepository userRepository;
	private final UserMapper mapper;
	private final AppMessages messages;
	private final Clock clock;

	@Inject
	public UserService(final UserRepository userRepository, final UserMapper mapper, AppMessages messages,
			final Clock clock) {
		this.userRepository = userRepository;
		this.mapper = mapper;
		this.messages = messages;
		this.clock = clock;
	}

	public List<UserResponse> findAll() {
		return mapper.toDtos(userRepository.listAll());
	}

	public UserResponse findById(Long userId) {
		User userById = userRepository.findByIdOptional(userId).orElseThrow(() -> {
			LOG.warn(messages.userWithIdNotFound(userId));
			return new NotFoundException(messages.userNotFound());
		});

		return mapper.toDto(userById);
	}

	public UserResponse findByUsername(String username) {
		User userByUsername = userRepository.findByUsernameOptional(username).orElseThrow(() -> {
			LOG.warn(messages.userWithUsernameNotFound(username));
			return new NotFoundException(messages.userNotFound());
		});

		return mapper.toDto(userByUsername);
	}

	public UserResponse create(CreateUserRequest createUserRequest) {
		boolean existsUserWithUsername = userRepository.findByUsernameOptional(createUserRequest.getUsername())
				.isPresent();
		if (Boolean.TRUE.equals(existsUserWithUsername)) {
			LOG.warn(messages.usernameAlreadyTakenDetailed(createUserRequest.getUsername()));
			throw new BadRequestException(messages.usernameAlreadyTaken());
		}

		boolean existsUserWithEmail = userRepository.findByEmailOptional(createUserRequest.getEmail()).isPresent();
		if (Boolean.TRUE.equals(existsUserWithEmail)) {
			LOG.warn(messages.emailAlreadyTakenDetailed(createUserRequest.getEmail()));
			throw new BadRequestException(messages.emailAlreadyTaken());
		}

		ZonedDateTime now = ZonedDateTime.now(clock);

		User user = mapper.toEntity(createUserRequest);
		user.setPassword(BcryptUtil.bcryptHash(user.getPassword()));
		user.setIsActive(Boolean.FALSE);
		user.setCreatedAt(now);
		user.setUpdatedAt(now);

		userRepository.persist(user);
		return mapper.toDto(user);
	}

	public UserResponse update(Long userId, UpdateUserRequest updateUserRequest) {
		User userById = userRepository.findByIdOptional(userId).orElseThrow(() -> {
			LOG.warn(messages.userWithIdNotFound(userId));
			return new NotFoundException(messages.userNotFound());
		});

		if (updateUserRequest.getName() != null)
			userById.setName(updateUserRequest.getName());

		if (updateUserRequest.getAge() != null)
			userById.setAge(updateUserRequest.getAge());

		userById.setUpdatedAt(ZonedDateTime.now(clock));
		userRepository.persist(userById);
		return mapper.toDto(userById);
	}

	public void deleteById(Long userId) {
		User userById = userRepository.findByIdOptional(userId).orElseThrow(() -> {
			LOG.warn(messages.userWithIdNotFound(userId));
			return new NotFoundException(messages.userNotFound());
		});

		userById.setIsActive(Boolean.FALSE);
		userById.setUpdatedAt(ZonedDateTime.now(clock));

		userRepository.persist(userById);
	}

	public void changePassword(Long userId, ChangePasswordRequest changePasswordRequest) {
		User userById = userRepository.findByIdOptional(userId).orElseThrow(() -> {
			LOG.warn(messages.userWithIdNotFound(userId));
			return new NotFoundException(messages.userNotFound());
		});

		if (!BcryptUtil.matches(changePasswordRequest.getCurrentPassword(), userById.getPassword())) {
			LOG.warn(messages.unauthorizedCredentialsDetailed(userById.getUsername()));
			throw new BadRequestException(messages.unauthorizedCredentials());
		}

		if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
			LOG.warn(messages.passwordsDoesntMatch());
			throw new BadRequestException(messages.passwordsDoesntMatch());
		}

		userById.setPassword(BcryptUtil.bcryptHash(changePasswordRequest.getNewPassword()));
		userById.setUpdatedAt(ZonedDateTime.now(clock));

		userRepository.persist(userById);
	}

}
