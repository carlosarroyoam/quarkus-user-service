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
import com.carlosarroyoam.userservice.model.Role;
import com.carlosarroyoam.userservice.model.User;
import com.carlosarroyoam.userservice.repository.RoleRepository;
import com.carlosarroyoam.userservice.repository.UserRepository;

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
	private final RoleRepository roleRepository;
	private final UserMapper mapper;
	private final AppMessages messages;
	private final Clock clock;

	@Inject
	public UserService(UserRepository userRepository, RoleRepository roleRepository, UserMapper mapper,
			AppMessages messages, Clock clock) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.mapper = mapper;
		this.messages = messages;
		this.clock = clock;
	}

	public List<UserResponse> findAll() {
		List<User> users = userRepository.listAll();
		return mapper.toDtos(users);
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

	@Transactional
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

		Role role = roleRepository.findByIdOptional(createUserRequest.getRoleId()).orElseThrow(() -> {
			LOG.warn(messages.roleNotFound());
			return new NotFoundException(messages.roleNotFound());
		});

		ZonedDateTime now = ZonedDateTime.now(clock);

		User user = mapper.toEntity(createUserRequest);
		user.setPassword(BcryptUtil.bcryptHash(user.getPassword()));
		user.setIsActive(Boolean.FALSE);
		user.setRole(role);
		user.setCreatedAt(now);
		user.setUpdatedAt(now);

		userRepository.persist(user);
		return mapper.toDto(user);
	}

	@Transactional
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

	@Transactional
	public void deleteById(Long userId) {
		User userById = userRepository.findByIdOptional(userId).orElseThrow(() -> {
			LOG.warn(messages.userWithIdNotFound(userId));
			return new NotFoundException(messages.userNotFound());
		});

		userById.setIsActive(Boolean.FALSE);
		userById.setUpdatedAt(ZonedDateTime.now(clock));

		userRepository.persist(userById);
	}

	@Transactional
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
