package com.carlosarroyoam.userservice.services;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.carlosarroyoam.userservice.repositories.UserRepository;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import jakarta.inject.Inject;

@QuarkusTest
class UserServiceTest {

	@Inject
	private UserService userService;

	@InjectMock
	private UserRepository userRepository;

	@Test
	@Disabled(value = "Not implemented yet")
	void testFindAllUsers() {
		fail("Not implemented yet");
	}

}
