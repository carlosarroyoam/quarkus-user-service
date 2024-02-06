package com.carlosarroyoam.userservice.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.carlosarroyoam.userservice.config.AppMessages;
import com.carlosarroyoam.userservice.dto.ChangePasswordRequest;
import com.carlosarroyoam.userservice.dto.CreateUserRequest;
import com.carlosarroyoam.userservice.dto.UpdateUserRequest;
import com.carlosarroyoam.userservice.dto.UserResponse;
import com.carlosarroyoam.userservice.model.User;
import com.carlosarroyoam.userservice.repository.UserRepository;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
class UserServiceTest {

	@Inject
	private UserService userService;

	@InjectMock
	private UserRepository userRepository;

	@Inject
	private AppMessages messages;

	@Test
	@DisplayName("Should return list of users")
	void testFindAllRetrievesListOfUsers() {
		Optional<User> user = createTestUser(false);
		Mockito.when(userRepository.listAll()).thenReturn(List.of(user.get()));

		List<UserResponse> usersDto = userService.findAll();

		assertThat(usersDto, hasSize(1));
		assertThat(usersDto.get(0).getId(), equalTo(user.get().getId()));
		assertThat(usersDto.get(0).getUsername(), equalTo(user.get().getUsername()));
	}

	@Test
	@DisplayName("Should return user when find user with existing id")
	void testFindByIdRetrievesUser() {
		Optional<User> user = createTestUser(false);
		Mockito.when(userRepository.findByIdOptional(Mockito.anyLong())).thenReturn(user);

		UserResponse userResponse = userService.findById(1L);

		assertThat(userResponse.getId(), equalTo(user.get().getId()));
		assertThat(userResponse.getUsername(), equalTo(user.get().getUsername()));
	}

	@Test
	@DisplayName("Should throw exception when find user with non existing id")
	void testFindByIdFailsWithNonExistingUser() {
		Mockito.when(userRepository.findByIdOptional(Mockito.anyLong())).thenReturn(Optional.empty());

		Throwable ex = assertThrows(NotFoundException.class, () -> userService.findById(1L));

		assertThat(ex.getMessage(), equalTo(messages.userNotFound()));
		assertThat(ex, instanceOf(NotFoundException.class));
	}

	@Test
	@DisplayName("Should return user when find user with existing username")
	void testFindByUsernameRetrievesUser() {
		Optional<User> user = createTestUser(false);
		Mockito.when(userRepository.findByUsernameOptional(Mockito.anyString())).thenReturn(user);

		UserResponse userResponse = userService.findByUsername("carroyom");

		assertThat(userResponse.getId(), equalTo(user.get().getId()));
		assertThat(userResponse.getUsername(), equalTo(user.get().getUsername()));
	}

	@Test
	@DisplayName("Should throw exception when find user with non existing username")
	void testFindByUsernameFailsWithNonExistingUsername() {
		Mockito.when(userRepository.findByUsernameOptional(Mockito.anyString())).thenReturn(Optional.empty());

		Throwable ex = assertThrows(NotFoundException.class, () -> userService.findByUsername("carroyom"));

		assertThat(ex.getMessage(), equalTo(messages.userNotFound()));
		assertThat(ex, instanceOf(NotFoundException.class));
	}

	@Test
	@DisplayName("Should create user with valid data")
	void testCreateUser() {
		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setEmail("carroyom@mail.com");
		createUserRequest.setUsername("carroyom");
		createUserRequest.setPassword("secret");

		Mockito.when(userRepository.findByUsernameOptional(Mockito.anyString())).thenReturn(Optional.empty());
		Mockito.when(userRepository.findByEmailOptional(Mockito.anyString())).thenReturn(Optional.empty());
		Mockito.doNothing().when(userRepository).persist(Mockito.any(User.class));

		UserResponse userResponse = userService.create(createUserRequest);

		assertThat(userResponse.getUsername(), equalTo(createUserRequest.getUsername()));
		assertThat(userResponse.getIsActive(), is(not(nullValue())));
		assertThat(userResponse.getCreatedAt(), is(not(nullValue())));
		assertThat(userResponse.getUpdatedAt(), is(not(nullValue())));
	}

	@Test
	@DisplayName("Should throw exception when create user with existing username")
	void testCreateUserFailsWithExistingUsername() {
		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setEmail("carroyom@mail.com");
		createUserRequest.setUsername("carroyom");
		createUserRequest.setPassword("secret");

		Mockito.when(userRepository.findByUsernameOptional(Mockito.anyString())).thenReturn(createTestUser(true));
		Mockito.when(userRepository.findByEmailOptional(Mockito.anyString())).thenReturn(Optional.empty());

		Throwable ex = assertThrows(BadRequestException.class, () -> userService.create(createUserRequest));

		assertThat(ex.getMessage(), equalTo(messages.usernameAlreadyTaken()));
		assertThat(ex, instanceOf(BadRequestException.class));
	}

	@Test
	@DisplayName("Should throw exception when create user with existing email")
	void testCreateUserFailsWithExistingEmail() {
		CreateUserRequest createUserRequest = new CreateUserRequest();
		createUserRequest.setEmail("carroyom@mail.com");
		createUserRequest.setUsername("carroyom");
		createUserRequest.setPassword("secret");

		Mockito.when(userRepository.findByUsernameOptional(Mockito.anyString())).thenReturn(Optional.empty());
		Mockito.when(userRepository.findByEmailOptional(Mockito.anyString())).thenReturn(createTestUser(true));

		Throwable ex = assertThrows(BadRequestException.class, () -> userService.create(createUserRequest));

		assertThat(ex.getMessage(), equalTo(messages.emailAlreadyTaken()));
		assertThat(ex, instanceOf(BadRequestException.class));
	}

	@Test
	@DisplayName("Should update user with valid data")
	void testUpdateUser() {
		UpdateUserRequest updateUserRequest = new UpdateUserRequest();
		updateUserRequest.setName("Carlos Arroyo Martínez");
		updateUserRequest.setAge(29);

		Optional<User> user = createTestUser(true);
		user.get().setName(updateUserRequest.getName());
		user.get().setAge(updateUserRequest.getAge());

		Mockito.when(userRepository.findByIdOptional(Mockito.anyLong())).thenReturn(user);
		Mockito.doNothing().when(userRepository).persist(Mockito.any(User.class));

		UserResponse userResponse = userService.update(1L, updateUserRequest);

		assertThat(userResponse.getName(), is(user.get().getName()));
		assertThat(userResponse.getAge(), is(user.get().getAge()));
	}

	@Test
	@DisplayName("Should throw exception when update non existing user")
	void testUpdateUserFailsWithNonExistingUser() {
		UpdateUserRequest updateUserRequest = new UpdateUserRequest();
		updateUserRequest.setName("Carlos Arroyo Martínez");
		updateUserRequest.setAge(29);

		Mockito.when(userRepository.findByIdOptional(Mockito.anyLong())).thenReturn(Optional.empty());

		Throwable ex = assertThrows(NotFoundException.class, () -> userService.update(1L, updateUserRequest));

		assertThat(ex.getMessage(), equalTo(messages.userNotFound()));
		assertThat(ex, instanceOf(NotFoundException.class));
	}

	@Test
	@DisplayName("Should delete a user")
	void testDeleteUser() {
		Mockito.when(userRepository.findByIdOptional(Mockito.anyLong())).thenReturn(createTestUser(true));

		userService.deleteById(1L);

		Mockito.verify(userRepository, Mockito.times(1)).persist(Mockito.any(User.class));
	}

	@Test
	@DisplayName("Should throw exception when delete a non existing user")
	void testDeleteUserFailsWithNonExistingUser() {
		Mockito.when(userRepository.findByIdOptional(Mockito.anyLong())).thenReturn(Optional.empty());

		Throwable ex = assertThrows(NotFoundException.class, () -> userService.deleteById(1L));

		assertThat(ex.getMessage(), equalTo(messages.userNotFound()));
		assertThat(ex, instanceOf(NotFoundException.class));
	}

	@Test
	@DisplayName("Should change user password with valid credentials")
	void testChangeUserPassword() {
		ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
		changePasswordRequest.setCurrentPassword("secret");
		changePasswordRequest.setNewPassword("new-secret");
		changePasswordRequest.setConfirmPassword("new-secret");

		Mockito.when(userRepository.findByIdOptional(Mockito.anyLong())).thenReturn(createTestUser(true));
		Mockito.doNothing().when(userRepository).persist(Mockito.any(User.class));

		userService.changePassword(1L, changePasswordRequest);

		Mockito.verify(userRepository, Mockito.times(1)).persist(Mockito.any(User.class));
	}

	@Test
	@DisplayName("Should throw exception when change user password with non valid credentials")
	void testChangeUserPasswordFailsWithNonValidCredentials() {
		ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
		changePasswordRequest.setCurrentPassword("non-valid-secret");
		changePasswordRequest.setNewPassword("new-secret");
		changePasswordRequest.setConfirmPassword("new-secret");

		Mockito.when(userRepository.findByIdOptional(Mockito.anyLong())).thenReturn(createTestUser(true));
		Mockito.doNothing().when(userRepository).persist(Mockito.any(User.class));

		Throwable ex = assertThrows(BadRequestException.class,
				() -> userService.changePassword(1L, changePasswordRequest));

		assertThat(ex.getMessage(), equalTo(messages.unauthorizedCredentials()));
		assertThat(ex, instanceOf(BadRequestException.class));
	}

	@Test
	@DisplayName("Should throw exception when change user password with non valid confirm password")
	void testChangeUserPasswordFailsWithNonValidConfirmPassword() {
		ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
		changePasswordRequest.setCurrentPassword("secret");
		changePasswordRequest.setNewPassword("new-secret");
		changePasswordRequest.setConfirmPassword("non-valid-new-secret");

		Mockito.when(userRepository.findByIdOptional(Mockito.anyLong())).thenReturn(createTestUser(true));
		Mockito.doNothing().when(userRepository).persist(Mockito.any(User.class));

		Throwable ex = assertThrows(BadRequestException.class,
				() -> userService.changePassword(1L, changePasswordRequest));

		assertThat(ex.getMessage(), equalTo(messages.passwordsDoesntMatch()));
		assertThat(ex, instanceOf(BadRequestException.class));
	}

	private Optional<User> createTestUser(Boolean isActive) {
		User user = new User();
		user.setId(1L);
		user.setUsername("carroyom");
		user.setEmail("carroyom@mail.com");
		user.setPassword("$2a$10$eAksNP3QN8numBgJwshVpOg2ywD5o6YxOW/4WCrk/dZmV77pC6QqC");
		user.setIsActive(isActive);

		return Optional.of(user);
	}

}
