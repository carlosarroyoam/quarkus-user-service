package com.carlosarroyoam.userservice.service;

import com.carlosarroyoam.userservice.config.AppMessages;
import com.carlosarroyoam.userservice.dto.ChangePasswordRequestDto;
import com.carlosarroyoam.userservice.dto.CreateUserRequestDto;
import com.carlosarroyoam.userservice.dto.UpdateUserRequestDto;
import com.carlosarroyoam.userservice.dto.UserDto;
import com.carlosarroyoam.userservice.entity.User;
import com.carlosarroyoam.userservice.mapper.UserMapper;
import com.carlosarroyoam.userservice.repository.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import org.jboss.logging.Logger;

@ApplicationScoped
public class UserService {
  private static final Logger LOG = Logger.getLogger(UserService.class);
  private final UserRepository userRepository;
  private final UserMapper mapper;
  private final AppMessages messages;
  private final Clock clock;

  @Inject
  public UserService(UserRepository userRepository, UserMapper mapper, AppMessages messages,
      Clock clock) {
    this.userRepository = userRepository;
    this.mapper = mapper;
    this.messages = messages;
    this.clock = clock;
  }

  public List<UserDto> findAll() {
    List<User> users = userRepository.listAll();
    return mapper.toDtos(users);
  }

  public UserDto findById(Long userId) {
    User userById = userRepository.findByIdOptional(userId).orElseThrow(() -> {
      LOG.warn(messages.userWithIdNotFound(userId));
      return new NotFoundException(messages.userNotFound());
    });

    return mapper.toDto(userById);
  }

  public UserDto findByUsername(String username) {
    User userByUsername = userRepository.findByUsernameOptional(username).orElseThrow(() -> {
      LOG.warn(messages.userWithUsernameNotFound(username));
      return new NotFoundException(messages.userNotFound());
    });

    return mapper.toDto(userByUsername);
  }

  @Transactional
  public UserDto create(CreateUserRequestDto requestDto) {
    if (userRepository.findByUsernameOptional(requestDto.getUsername()).isPresent()) {
      LOG.warn(messages.usernameAlreadyTakenDetailed(requestDto.getUsername()));
      throw new BadRequestException(messages.usernameAlreadyTaken());
    }

    if (userRepository.findByEmailOptional(requestDto.getEmail()).isPresent()) {
      LOG.warn(messages.emailAlreadyTakenDetailed(requestDto.getEmail()));
      throw new BadRequestException(messages.emailAlreadyTaken());
    }

    LocalDateTime now = LocalDateTime.now(clock);

    User user = mapper.toEntity(requestDto);
    user.setPassword(BcryptUtil.bcryptHash(user.getPassword()));
    user.setIsActive(Boolean.FALSE);
    user.setCreatedAt(now);
    user.setUpdatedAt(now);

    userRepository.persist(user);
    return mapper.toDto(user);
  }

  @Transactional
  public void update(Long userId, UpdateUserRequestDto requestDto) {
    User userById = userRepository.findByIdOptional(userId).orElseThrow(() -> {
      LOG.warn(messages.userWithIdNotFound(userId));
      return new NotFoundException(messages.userNotFound());
    });

    if (requestDto.getName() != null) {
      userById.setName(requestDto.getName());
    }

    if (requestDto.getAge() != null) {
      userById.setAge(requestDto.getAge());
    }

    userById.setUpdatedAt(LocalDateTime.now(clock));
    userRepository.persist(userById);
  }

  @Transactional
  public void deleteById(Long userId) {
    User userById = userRepository.findByIdOptional(userId).orElseThrow(() -> {
      LOG.warn(messages.userWithIdNotFound(userId));
      return new NotFoundException(messages.userNotFound());
    });

    userById.setIsActive(Boolean.FALSE);
    userById.setUpdatedAt(LocalDateTime.now(clock));

    userRepository.persist(userById);
  }

  @Transactional
  public void changePassword(Long userId, ChangePasswordRequestDto requestDto) {
    User userById = userRepository.findByIdOptional(userId).orElseThrow(() -> {
      LOG.warn(messages.userWithIdNotFound(userId));
      return new NotFoundException(messages.userNotFound());
    });

    if (!BcryptUtil.matches(requestDto.getCurrentPassword(), userById.getPassword())) {
      LOG.warn(messages.unauthorizedCredentialsDetailed(userById.getUsername()));
      throw new BadRequestException(messages.unauthorizedCredentials());
    }

    if (!requestDto.getNewPassword().equals(requestDto.getConfirmPassword())) {
      LOG.warn(messages.passwordsDoesntMatch());
      throw new BadRequestException(messages.passwordsDoesntMatch());
    }

    userById.setPassword(BcryptUtil.bcryptHash(requestDto.getNewPassword()));
    userById.setUpdatedAt(LocalDateTime.now(clock));

    userRepository.persist(userById);
  }
}
