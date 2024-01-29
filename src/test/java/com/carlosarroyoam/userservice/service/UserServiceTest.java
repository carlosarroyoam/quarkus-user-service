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

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.carlosarroyoam.userservice.constant.AppMessages;
import com.carlosarroyoam.userservice.dto.CreateUserDto;
import com.carlosarroyoam.userservice.dto.UserDto;
import com.carlosarroyoam.userservice.model.User;
import com.carlosarroyoam.userservice.repository.UserRepository;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

@QuarkusTest
class UserServiceTest {

	@Inject
	private UserService userService;

	@InjectMock
	private UserRepository userRepository;

	@Test
	void testFindAllRetrievesListOfUsers() {
		Optional<User> user = createTestUser(false);
		Mockito.when(userRepository.listAll()).thenReturn(List.of(user.get()));
		List<UserDto> usersDto = userService.findAll();

		assertThat(usersDto, hasSize(1));
		assertThat(usersDto.get(0).getId(), equalTo(user.get().getId()));
		assertThat(usersDto.get(0).getUsername(), equalTo(user.get().getUsername()));
	}

	@Test
	void testFindByIdRetrievesUser() {
		Optional<User> user = createTestUser(false);
		Mockito.when(userRepository.findByIdOptional(Mockito.anyLong())).thenReturn(user);

		UserDto userDto = userService.findById(1l);

		assertThat(userDto.getId(), equalTo(user.get().getId()));
		assertThat(userDto.getUsername(), equalTo(user.get().getUsername()));
	}

	@Test
	void testFindByIdFailsWithNonExistingUser() {
		Mockito.when(userRepository.findByIdOptional(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));

		Throwable ex = assertThrows(NotFoundException.class, () -> userService.findById(1l));

		assertThat(ex.getMessage(), equalTo(AppMessages.USER_ID_NOT_FOUND_EXCEPTION_MESSAGE));
		assertThat(ex, instanceOf(NotFoundException.class));
	}

	@Test
	void testFindByUsernameRetrievesUser() {
		Optional<User> user = createTestUser(false);
		Mockito.when(userRepository.findByUsernameOptional(Mockito.anyString())).thenReturn(user);

		UserDto userDto = userService.findByUsername("carroyom");

		assertThat(userDto.getId(), equalTo(user.get().getId()));
		assertThat(userDto.getUsername(), equalTo(user.get().getUsername()));
	}

	@Test
	void testFindByUsernameFailsWithNonExistingUsername() {
		Mockito.when(userRepository.findByUsernameOptional(Mockito.anyString())).thenReturn(Optional.ofNullable(null));

		Throwable ex = assertThrows(NotFoundException.class, () -> userService.findByUsername("carroyom"));

		assertThat(ex.getMessage(), equalTo(AppMessages.USER_USERNAME_NOT_FOUND_EXCEPTION_MESSAGE));
		assertThat(ex, instanceOf(NotFoundException.class));
	}

	@Test
	void testCreateUser() {
		CreateUserDto createUserDto = createTestCreateUserDto();
		Mockito.when(userRepository.findByUsernameOptional(Mockito.anyString())).thenReturn(Optional.ofNullable(null));
		Mockito.when(userRepository.findByEmailOptional(Mockito.anyString())).thenReturn(Optional.ofNullable(null));
		Mockito.doNothing().when(userRepository).persist(Mockito.any(User.class));

		UserDto userDto = userService.create(createUserDto);

		assertThat(userDto.getUsername(), equalTo(createUserDto.getUsername()));
		assertThat(userDto.getIsActive(), is(not(nullValue())));
		assertThat(userDto.getCreatedAt(), is(not(nullValue())));
		assertThat(userDto.getUpdatedAt(), is(not(nullValue())));
	}

	@Test
	void testCreateUserFailsWithExistingUsername() {
		CreateUserDto createUserDto = createTestCreateUserDto();
		Mockito.when(userRepository.findByUsernameOptional(Mockito.anyString())).thenReturn(createTestUser(true));
		Mockito.when(userRepository.findByEmailOptional(Mockito.anyString())).thenReturn(Optional.ofNullable(null));

		Throwable ex = assertThrows(BadRequestException.class, () -> userService.create(createUserDto));

		assertThat(ex.getMessage(), equalTo(AppMessages.USERNAME_ALREADY_EXISTS_EXCEPTION_MESSAGE));
		assertThat(ex, instanceOf(BadRequestException.class));
	}

	@Test
	void testCreateUserFailsWithExistingMail() {
		CreateUserDto createUserDto = createTestCreateUserDto();
		Mockito.when(userRepository.findByUsernameOptional(Mockito.anyString())).thenReturn(Optional.ofNullable(null));
		Mockito.when(userRepository.findByEmailOptional(Mockito.anyString())).thenReturn(createTestUser(true));

		Throwable ex = assertThrows(BadRequestException.class, () -> userService.create(createUserDto));

		assertThat(ex.getMessage(), equalTo(AppMessages.EMAIL_ALREADY_EXISTS_EXCEPTION_MESSAGE));
		assertThat(ex, instanceOf(BadRequestException.class));
	}

	private Optional<User> createTestUser(Boolean isActive) {
		User user = new User();
		user.setId(1l);
		user.setUsername("carroyom");
		user.setEmail("carroyom@mail.com");
		user.setPassword("$2a$10$eAksNP3QN8numBgJwshVpOg2ywD5o6YxOW/4WCrk/dZmV77pC6QqC");
		user.setIsActive(isActive);
		return Optional.of(user);
	}

	private CreateUserDto createTestCreateUserDto() {
		CreateUserDto createUserDto = new CreateUserDto();
		createUserDto.setName("Carlos Alberto Arroyo Martínez");
		createUserDto.setEmail("carroyom@mail.com");
		createUserDto.setUsername("carroyom");
		createUserDto.setPassword("secret");
		createUserDto.setRole("Admin,User");
		createUserDto.setAge(28);
		return createUserDto;
	}
}
