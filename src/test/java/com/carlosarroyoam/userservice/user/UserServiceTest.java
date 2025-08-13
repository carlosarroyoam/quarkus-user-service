package com.carlosarroyoam.userservice.user;

import com.carlosarroyoam.userservice.core.config.AppMessages;
import com.carlosarroyoam.userservice.user.dto.ChangePasswordRequestDto;
import com.carlosarroyoam.userservice.user.dto.CreateUserRequestDto;
import com.carlosarroyoam.userservice.user.dto.UpdateUserRequestDto;
import com.carlosarroyoam.userservice.user.dto.UserDto;
import com.carlosarroyoam.userservice.user.entity.Role;
import com.carlosarroyoam.userservice.user.entity.User;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class UserServiceTest {
  @Inject
  private UserService userService;

  @InjectMock
  private UserRepository userRepository;

  @InjectMock
  private RoleRepository roleRepository;

  @Inject
  private AppMessages messages;

  @Test
  @DisplayName("Should return List<UserResponse> when find all")
  void shouldReturnUsersListWhenFindAll() {
    Optional<User> user = createUser(false);

    Mockito.when(userRepository.listAll()).thenReturn(List.of(user.get()));

    List<UserDto> usersDto = userService.findAll();

    assertThat(usersDto, hasSize(1));
    assertThat(usersDto.get(0).getId(), equalTo(user.get().getId()));
    assertThat(usersDto.get(0).getUsername(), equalTo(user.get().getUsername()));
  }

  @Test
  @DisplayName("Should return UserResponse when find user with existing userId")
  void shouldReturnUserResponseWhenFindUserWithExistingId() {
    Optional<User> user = createUser(false);

    Mockito.when(userRepository.findByIdOptional(ArgumentMatchers.anyLong())).thenReturn(user);

    UserDto userDto = userService.findById(1L);

    assertThat(userDto.getId(), equalTo(user.get().getId()));
    assertThat(userDto.getUsername(), equalTo(user.get().getUsername()));
  }

  @Test
  @DisplayName("Should throw exception when find user with non existing id")
  void shouldThrowExceptionWhenFindUserWithNonExistingId() {
    Mockito.when(userRepository.findByIdOptional(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.empty());

    Throwable ex = assertThrows(NotFoundException.class, () -> userService.findById(1L));

    assertThat(ex.getMessage(), equalTo(messages.userNotFound()));
    assertThat(ex, instanceOf(NotFoundException.class));
  }

  @Test
  @DisplayName("Should return UserResponse when find user with existing username")
  void shouldReturnUserResponseWhenFindUserWithExistingUsername() {
    Optional<User> user = createUser(false);

    Mockito.when(userRepository.findByUsernameOptional(ArgumentMatchers.anyString()))
        .thenReturn(user);

    UserDto userDto = userService.findByUsername("carroyom");

    assertThat(userDto.getId(), equalTo(user.get().getId()));
    assertThat(userDto.getUsername(), equalTo(user.get().getUsername()));
  }

  @Test
  @DisplayName("Should throw exception when find user with non existing username")
  void shouldThrowExceptionWhenFindUserWithNonExistingUsername() {
    Mockito.when(userRepository.findByUsernameOptional(ArgumentMatchers.anyString()))
        .thenReturn(Optional.empty());

    Throwable ex = assertThrows(NotFoundException.class,
        () -> userService.findByUsername("carroyom"));

    assertThat(ex.getMessage(), equalTo(messages.userNotFound()));
    assertThat(ex, instanceOf(NotFoundException.class));
  }

  @Test
  @DisplayName("Should create user when valid data is provided")
  void shouldCreateUserWhenProvideValidData() {
    CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
        .email("carroyom@mail.com")
        .username("carroyom")
        .password("secret")
        .roleId(1)
        .build();

    Mockito.when(userRepository.findByUsernameOptional(ArgumentMatchers.anyString()))
        .thenReturn(Optional.empty());

    Mockito.when(userRepository.findByEmailOptional(ArgumentMatchers.anyString()))
        .thenReturn(Optional.empty());

    Mockito.doNothing().when(userRepository).persist(ArgumentMatchers.any(User.class));

    UserDto userDto = userService.create(createUserRequestDto);

    assertThat(userDto.getUsername(), is(not(nullValue())));
    assertThat(userDto.getIsActive(), is(not(nullValue())));
    assertThat(userDto.getCreatedAt(), is(not(nullValue())));
    assertThat(userDto.getUpdatedAt(), is(not(nullValue())));
  }

  @Test
  @DisplayName("Should throw exception when create user with existing username")
  void shouldThrowExceptionWhenCreateUserWithExistingUsername() {
    CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
        .email("carroyom@mail.com")
        .username("carroyom")
        .password("secret")
        .build();

    Mockito.when(userRepository.findByUsernameOptional(ArgumentMatchers.anyString()))
        .thenReturn(createUser(true));
    Mockito.when(userRepository.findByEmailOptional(ArgumentMatchers.anyString()))
        .thenReturn(Optional.empty());

    Throwable ex = assertThrows(BadRequestException.class,
        () -> userService.create(createUserRequestDto));

    assertThat(ex.getMessage(), equalTo(messages.usernameAlreadyTaken()));
    assertThat(ex, instanceOf(BadRequestException.class));
  }

  @Test
  @DisplayName("Should throw exception when create user with existing email")
  void shouldThrowExceptionWhenCreateUserWithExistingEmail() {
    CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
        .email("carroyom@mail.com")
        .username("carroyom")
        .password("secret")
        .build();

    Mockito.when(userRepository.findByUsernameOptional(ArgumentMatchers.anyString()))
        .thenReturn(Optional.empty());

    Mockito.when(userRepository.findByEmailOptional(ArgumentMatchers.anyString()))
        .thenReturn(createUser(true));

    Throwable ex = assertThrows(BadRequestException.class,
        () -> userService.create(createUserRequestDto));

    assertThat(ex.getMessage(), equalTo(messages.emailAlreadyTaken()));
    assertThat(ex, instanceOf(BadRequestException.class));
  }

  @Test
  @DisplayName("Should update user with valid data")
  void shouldUpdateUserWhenProvideValidData() {
    UpdateUserRequestDto updateUserRequestDto = UpdateUserRequestDto.builder()
        .name("Carlos Arroyo Martínez")
        .age(Byte.valueOf("29"))
        .build();

    Optional<User> user = createUser(true);
    user.get().setName(updateUserRequestDto.getName());
    user.get().setAge(updateUserRequestDto.getAge());

    Mockito.when(userRepository.findByIdOptional(ArgumentMatchers.anyLong())).thenReturn(user);

    Mockito.doNothing().when(userRepository).persist(ArgumentMatchers.any(User.class));

    userService.update(1L, updateUserRequestDto);

    Mockito.verify(userRepository, Mockito.times(1)).persist(ArgumentMatchers.any(User.class));
  }

  @Test
  @DisplayName("Should throw exception when update non existing user")
  void shouldThrowExceptionWhenUpdateWithNonExistingUser() {
    UpdateUserRequestDto updateUserRequestDto = UpdateUserRequestDto.builder()
        .name("Carlos Arroyo Martínez")
        .age(Byte.valueOf("29"))
        .build();

    Mockito.when(userRepository.findByIdOptional(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.empty());

    Throwable ex = assertThrows(NotFoundException.class,
        () -> userService.update(1L, updateUserRequestDto));

    assertThat(ex.getMessage(), equalTo(messages.userNotFound()));
    assertThat(ex, instanceOf(NotFoundException.class));
  }

  @Test
  @DisplayName("Should delete a user")
  void shouldDeleteUserWhenProvideExistingId() {
    Mockito.when(userRepository.findByIdOptional(ArgumentMatchers.anyLong()))
        .thenReturn(createUser(true));

    userService.deleteById(1L);

    Mockito.verify(userRepository, Mockito.times(1)).persist(ArgumentMatchers.any(User.class));
  }

  @Test
  @DisplayName("Should throw exception when delete a non existing user")
  void shouldThrowExceptionWhenDeleteWithNonExistingUser() {
    Mockito.when(userRepository.findByIdOptional(ArgumentMatchers.anyLong()))
        .thenReturn(Optional.empty());

    Throwable ex = assertThrows(NotFoundException.class, () -> userService.deleteById(1L));

    assertThat(ex.getMessage(), equalTo(messages.userNotFound()));
    assertThat(ex, instanceOf(NotFoundException.class));
  }

  @Test
  @DisplayName("Should change user password with valid credentials")
  void shouldChangePasswordWhenProvideValidCredentials() {
    ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
        .currentPassword("secret")
        .newPassword("new-secret")
        .confirmPassword("new-secret")
        .build();

    Mockito.when(userRepository.findByIdOptional(ArgumentMatchers.anyLong()))
        .thenReturn(createUser(true));

    Mockito.doNothing().when(userRepository).persist(ArgumentMatchers.any(User.class));

    userService.changePassword(1L, changePasswordRequestDto);

    Mockito.verify(userRepository, Mockito.times(1)).persist(ArgumentMatchers.any(User.class));
  }

  @Test
  @DisplayName("Should throw exception when change user password with non valid credentials")
  void shouldThrowExceptionWhenChangePasswordWithNonValidCredentials() {
    ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
        .currentPassword("non-valid-secret")
        .newPassword("new-secret")
        .confirmPassword("new-secret")
        .build();

    Mockito.when(userRepository.findByIdOptional(ArgumentMatchers.anyLong()))
        .thenReturn(createUser(true));

    Mockito.doNothing().when(userRepository).persist(ArgumentMatchers.any(User.class));

    Throwable ex = assertThrows(BadRequestException.class,
        () -> userService.changePassword(1L, changePasswordRequestDto));

    assertThat(ex.getMessage(), equalTo(messages.unauthorizedCredentials()));
    assertThat(ex, instanceOf(BadRequestException.class));
  }

  @Test
  @DisplayName("Should throw exception when change user password with non valid confirm password")
  void shouldThrowExceptionWhenChangePasswordWithNonValidConfirmPassword() {
    ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
        .currentPassword("secret")
        .newPassword("new-secret")
        .confirmPassword("non-valid-new-secret")
        .build();

    Mockito.when(userRepository.findByIdOptional(ArgumentMatchers.anyLong()))
        .thenReturn(createUser(true));

    Mockito.doNothing().when(userRepository).persist(ArgumentMatchers.any(User.class));

    Throwable ex = assertThrows(BadRequestException.class,
        () -> userService.changePassword(1L, changePasswordRequestDto));

    assertThat(ex.getMessage(), equalTo(messages.passwordsDoesntMatch()));
    assertThat(ex, instanceOf(BadRequestException.class));
  }

  private Optional<User> createUser(Boolean isActive) {
    Role role = Role.builder().id(1).title("Admin").description("Role for admins users").build();

    User user = User.builder()
        .id(1L)
        .username("carroyom")
        .email("carroyom@mail.com")
        .password("$2a$10$eAksNP3QN8numBgJwshVpOg2ywD5o6YxOW/4WCrk/dZmV77pC6QqC")
        .isActive(isActive)
        .role(role)
        .roleId(role.getId())
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();

    return Optional.of(user);
  }
}
