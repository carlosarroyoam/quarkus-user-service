package com.carlosarroyoam.userservice.services;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.carlosarroyoam.userservice.repositories.UserRepository;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import jakarta.inject.Inject;

@QuarkusTest
class UserServiceTest {

	@Inject
	UserService userService;

	@InjectMock
	private UserRepository userRepository;

	@Test
	@Disabled(value = "not implemented yet")
	void testFindAllUsers() {
		assertThat("", notNullValue());
	}

}
