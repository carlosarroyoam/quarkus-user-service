package com.carlosarroyoam.userservice.services;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.carlosarroyoam.userservice.constants.AppMessages;
import com.carlosarroyoam.userservice.model.User;
import com.carlosarroyoam.userservice.repositories.UserRepository;

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
		List<User> actualUsers = userService.findAll();

		assertThat(actualUsers, hasSize(0));
	}

	@Test
	void testFindByIdRetrievesUser() {
		User expectedUser = getUser(false);
		Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(expectedUser);

		User actualUser = userService.findById(1l);

		assertThat(actualUser.getId(), equalTo(expectedUser.getId()));
		assertThat(actualUser.getUsername(), equalTo(expectedUser.getUsername()));
	}

	@Test
	void testFindByIdFailsWithNonExistingUser() {
		Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(null);

		Throwable ex = assertThrows(NotFoundException.class, () -> userService.findById(1l));

		assertThat(ex.getMessage(), equalTo(AppMessages.USER_ID_NOT_FOUND_EXCEPTION_MESSAGE));
		assertThat(ex, instanceOf(NotFoundException.class));
	}

	@Test
	void testFindByUsernameRetrievesUser() {
		User expectedUser = getUser(false);
		Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(expectedUser);

		User actualUser = userService.findByUsername("carroyom");

		assertThat(actualUser.getId(), equalTo(expectedUser.getId()));
		assertThat(actualUser.getUsername(), equalTo(expectedUser.getUsername()));
	}

	@Test
	void testFindByUsernameFailsWithNonExistingUsername() {
		Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(null);

		Throwable ex = assertThrows(NotFoundException.class, () -> userService.findByUsername(""));

		assertThat(ex.getMessage(), equalTo(AppMessages.USER_USERNAME_NOT_FOUND_EXCEPTION_MESSAGE));
		assertThat(ex, instanceOf(NotFoundException.class));
	}

	@Test
	void testCreateUser() {
		User expectedUser = getUserRequest();
		Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(null);
		Mockito.when(userRepository.findByMail(Mockito.anyString())).thenReturn(null);
		Mockito.doNothing().when(userRepository).persist(Mockito.any(User.class));

		User actualUser = userService.create(expectedUser);

		assertThat(actualUser.getId(), equalTo(expectedUser.getId()));
		assertThat(actualUser.getUsername(), equalTo(expectedUser.getUsername()));
		assertThat(actualUser.getPassword(), is(not(nullValue())));
		assertThat(actualUser.getIsActive(), is(not(nullValue())));
		assertThat(actualUser.getCreatedAt(), is(not(nullValue())));
		assertThat(actualUser.getUpdatedAt(), is(not(nullValue())));
	}

	@Test
	void testCreateUserFailsWithExistingUsername() {
		User expectedUser = getUserRequest();
		Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(getUser(true));
		Mockito.when(userRepository.findByMail(Mockito.anyString())).thenReturn(null);

		Throwable ex = assertThrows(BadRequestException.class, () -> userService.create(expectedUser));

		assertThat(ex.getMessage(), equalTo(AppMessages.USERNAME_ALREADY_EXISTS_EXCEPTION_MESSAGE));
		assertThat(ex, instanceOf(BadRequestException.class));
	}

	@Test
	void testCreateUserFailsWithExistingMail() {
		User expectedUser = getUserRequest();
		Mockito.when(userRepository.findByUsername(Mockito.anyString())).thenReturn(null);
		Mockito.when(userRepository.findByMail(Mockito.anyString())).thenReturn(getUser(true));

		Throwable ex = assertThrows(BadRequestException.class, () -> userService.create(expectedUser));

		assertThat(ex.getMessage(), equalTo(AppMessages.MAIL_ALREADY_EXISTS_EXCEPTION_MESSAGE));
		assertThat(ex, instanceOf(BadRequestException.class));
	}

	private User getUser(Boolean isActive) {
		User user = new User();
		user.setId(1l);
		user.setUsername("carroyom");
		user.setMail("carroyom@mail.com");
		user.setPassword("$2a$10$eAksNP3QN8numBgJwshVpOg2ywD5o6YxOW/4WCrk/dZmV77pC6QqC");
		user.setIsActive(isActive);
		return user;
	}

	private User getUserRequest() {
		User user = new User();
		user.setName("Carlos Alberto Arroyo Martínez");
		user.setMail("carroyom@mail.com");
		user.setUsername("carroyom");
		user.setPassword("secret");
		user.setRole("Admin,User");
		user.setAge(28);
		return user;
	}
}
