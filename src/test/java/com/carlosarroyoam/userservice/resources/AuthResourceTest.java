package com.carlosarroyoam.userservice.resources;

import org.junit.jupiter.api.Test;

import com.carlosarroyoam.userservice.dto.LoginRequest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response.Status;

import static io.restassured.RestAssured.given;

@QuarkusTest
class AuthResourceTest {

	@Test
	void testAuthEndpointWithCorrectUserCredentials() {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("carroyom");
		loginRequest.setPassword("secret");

		given()
		.contentType(ContentType.JSON)
		.body(loginRequest)
		.when().post("/auth")
		.then()
		.statusCode(Status.OK.getStatusCode());
	}

	@Test
	void testAuthEndpointWithIncorrectUserCredentials() {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("nonExist");
		loginRequest.setPassword("nonExist");

		given()
		.contentType(ContentType.JSON)
		.body(loginRequest)
		.when().post("/auth")
		.then()
		.statusCode(Status.UNAUTHORIZED.getStatusCode());
	}

}
