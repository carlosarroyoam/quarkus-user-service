package com.carlosarroyoam.userservice.user;

import com.carlosarroyoam.userservice.core.config.AppMessages;
import com.carlosarroyoam.userservice.user.dto.ChangePasswordRequestDto;
import com.carlosarroyoam.userservice.user.dto.CreateUserRequestDto;
import com.carlosarroyoam.userservice.user.dto.UpdateUserRequestDto;
import com.carlosarroyoam.userservice.user.dto.UserDto;
import com.carlosarroyoam.userservice.user.dto.UserDto.UserDtoMapper;
import com.carlosarroyoam.userservice.user.entity.User;
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
import org.jspecify.annotations.NonNull;

/**
 * Contiene la lógica de negocio de {@link User}: consulta por identificador o nombre de usuario,
 * alta validando la unicidad de usuario y correo, actualización de datos, baja lógica y cambio de
 * contraseña.
 */
@ApplicationScoped
public class UserService {
  private static final Logger LOG = Logger.getLogger(UserService.class);
  private final UserRepository userRepository;
  private final AppMessages messages;
  private final Clock clock;

  /**
   * Crea el servicio con sus dependencias.
   *
   * @param userRepository repositorio de usuarios
   * @param messages mensajes localizados de la aplicación
   * @param clock reloj utilizado para las marcas de tiempo
   */
  @Inject
  public UserService(UserRepository userRepository, AppMessages messages, Clock clock) {
    this.userRepository = userRepository;
    this.messages = messages;
    this.clock = clock;
  }

  /**
   * Busca todos los usuarios.
   *
   * @return la lista de usuarios encontrados
   */
  public List<UserDto> findAll() {
    List<User> users = userRepository.listAll();
    return UserDtoMapper.INSTANCE.toDtos(users);
  }

  /**
   * Busca un usuario por su identificador.
   *
   * @param userId identificador del usuario
   * @return el usuario encontrado
   * @throws NotFoundException si no existe un usuario con ese identificador
   */
  public UserDto findById(Long userId) {
    User userById = findUserById(userId);
    return UserDtoMapper.INSTANCE.toDto(userById);
  }

  /**
   * Busca un usuario por su nombre de usuario.
   *
   * @param username nombre de usuario
   * @return el usuario encontrado
   * @throws NotFoundException si no existe un usuario con ese nombre de usuario
   */
  public UserDto findByUsername(String username) {
    User userByUsername = findUserByUsername(username);
    return UserDtoMapper.INSTANCE.toDto(userByUsername);
  }

  /**
   * Crea un usuario inactivo con la contraseña cifrada con bcrypt. El nombre de usuario y el correo
   * deben ser únicos.
   *
   * @param requestDto datos del usuario a crear
   * @return el usuario creado
   * @throws BadRequestException si el nombre de usuario o el correo ya están en uso
   */
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
    User user = UserDtoMapper.INSTANCE.toEntity(requestDto);
    user.setPassword(BcryptUtil.bcryptHash(user.getPassword()));
    user.setIsActive(Boolean.FALSE);
    user.setCreatedAt(now);
    user.setUpdatedAt(now);
    userRepository.persist(user);

    return UserDtoMapper.INSTANCE.toDto(user);
  }

  /**
   * Actualiza el nombre y la edad de un usuario.
   *
   * @param userId identificador del usuario
   * @param requestDto nuevos datos del usuario
   * @throws NotFoundException si no existe un usuario con ese identificador
   */
  @Transactional
  public void update(Long userId, UpdateUserRequestDto requestDto) {
    User userById = findUserById(userId);
    userById.setName(requestDto.getName());
    userById.setAge(requestDto.getAge());
    userById.setUpdatedAt(LocalDateTime.now(clock));
    userRepository.persist(userById);
  }

  /**
   * Desactiva un usuario. La baja es lógica: el registro se conserva con {@code isActive} en {@code
   * false}.
   *
   * @param userId identificador del usuario
   * @throws NotFoundException si no existe un usuario con ese identificador
   */
  @Transactional
  public void deleteById(Long userId) {
    User userById = findUserById(userId);
    userById.setIsActive(Boolean.FALSE);
    userById.setUpdatedAt(LocalDateTime.now(clock));
    userRepository.persist(userById);
  }

  /**
   * Cambia la contraseña de un usuario. La contraseña actual debe ser correcta y la nueva debe
   * coincidir con su confirmación.
   *
   * @param userId identificador del usuario
   * @param requestDto contraseña actual, contraseña nueva y su confirmación
   * @throws NotFoundException si no existe un usuario con ese identificador
   * @throws BadRequestException si la contraseña actual es incorrecta o la nueva no coincide con su
   *     confirmación
   */
  @Transactional
  public void changePassword(Long userId, ChangePasswordRequestDto requestDto) {
    User userById = findUserById(userId);

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

  /**
   * Busca un usuario por su identificador.
   *
   * @param userId identificador del usuario
   * @return el usuario encontrado
   * @throws NotFoundException si no existe un usuario con ese identificador
   */
  private @NonNull User findUserById(Long userId) {
    return userRepository
        .findByIdOptional(userId)
        .orElseThrow(() -> {
          LOG.warn(messages.userWithIdNotFound(userId));
          return new NotFoundException(messages.userNotFound());
        });
  }

  /**
   * Busca un usuario por su nombre de usuario.
   *
   * @param username nombre de usuario
   * @return el usuario encontrado
   * @throws NotFoundException si no existe un usuario con ese nombre de usuario
   */
  private @NonNull User findUserByUsername(String username) {
    return userRepository
        .findByUsernameOptional(username)
        .orElseThrow(() -> {
          LOG.warn(messages.userWithUsernameNotFound(username));
          return new NotFoundException(messages.userNotFound());
        });
  }
}
