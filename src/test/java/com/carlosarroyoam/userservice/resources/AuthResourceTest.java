package com.carlosarroyoam.userservice.resources;

import static org.hamcrest.CoreMatchers.*;

import org.junit.jupiter.api.Test;

import com.carlosarroyoam.userservice.dto.LoginRequestDto;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

import static io.restassured.RestAssured.given;

@QuarkusTest
class AuthResourceTest {

	@Test
	void testAuthEndpointWithCorrectUserCredentials() {
		LoginRequestDto loginRequest = new LoginRequestDto();
		loginRequest.setUsername("carroyom");
		loginRequest.setPassword("secret");

		given()
		.with().contentType(MediaType.APPLICATION_JSON).body(loginRequest)
		.when().post("/auth")
		.then()
		.statusCode(Status.OK.getStatusCode())
		.body("username", is(equalTo(loginRequest.getUsername())))
		.body("accessToken", is(notNullValue()));
	}

	@Test
	void testAuthEndpointWithIncorrectUserCredentials() {
		LoginRequestDto loginRequest = new LoginRequestDto();
		loginRequest.setUsername("nonExist");
		loginRequest.setPassword("nonExist");

		given()
		.with().contentType(MediaType.APPLICATION_JSON).body(loginRequest)
		.when().post("/auth")
		.then()
		.statusCode(Status.UNAUTHORIZED.getStatusCode());
	}

}
