package com.carlosarroyoam.userservice.service;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.List;

import org.jboss.logging.Logger;

import com.carlosarroyoam.userservice.constant.AppMessages;
import com.carlosarroyoam.userservice.dto.CreateUserDto;
import com.carlosarroyoam.userservice.dto.UserDto;
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
	private final Clock clock;

	@Inject
	public UserService(final UserRepository userRepository, final UserMapper mapper, final Clock clock) {
		this.userRepository = userRepository;
		this.mapper = mapper;
		this.clock = clock;
	}

	public List<UserDto> findAll() {
		return mapper.toDtos(userRepository.listAll());
	}

	public UserDto findById(Long id) {
		User userById = userRepository.findByIdOptional(id)
				.orElseThrow(() -> new NotFoundException(AppMessages.USER_ID_NOT_FOUND_EXCEPTION_MESSAGE));

		return mapper.toDto(userById);
	}

	public UserDto findByUsername(String username) {
		User userByUsername = userRepository.findByUsernameOptional(username)
				.orElseThrow(() -> new NotFoundException(AppMessages.USER_USERNAME_NOT_FOUND_EXCEPTION_MESSAGE));

		return mapper.toDto(userByUsername);
	}

	public UserDto create(CreateUserDto createUserDto) {
		boolean existsUserWithUsername = userRepository.findByUsernameOptional(createUserDto.getUsername()).isPresent();

		if (Boolean.TRUE.equals(existsUserWithUsername)) {
			LOG.errorf(AppMessages.USERNAME_ALREADY_EXISTS_EXCEPTION_DETAILED_MESSAGE, createUserDto.getUsername());
			throw new BadRequestException(AppMessages.USERNAME_ALREADY_EXISTS_EXCEPTION_MESSAGE);
		}

		boolean existsUserWithEmail = userRepository.findByEmailOptional(createUserDto.getEmail()).isPresent();

		if (Boolean.TRUE.equals(existsUserWithEmail)) {
			LOG.errorf(AppMessages.EMAIL_ALREADY_EXISTS_EXCEPTION_DETAILED_MESSAGE, createUserDto.getUsername());
			throw new BadRequestException(AppMessages.EMAIL_ALREADY_EXISTS_EXCEPTION_MESSAGE);
		}

		User user = mapper.toEntity(createUserDto);
		ZonedDateTime now = ZonedDateTime.now(clock);

		user.setPassword(BcryptUtil.bcryptHash(user.getPassword()));
		user.setIsActive(Boolean.FALSE);
		user.setCreatedAt(now);
		user.setUpdatedAt(now);

		userRepository.persist(user);

		return mapper.toDto(user);
	}

}
