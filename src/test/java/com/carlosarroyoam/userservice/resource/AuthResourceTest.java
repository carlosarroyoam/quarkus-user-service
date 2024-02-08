package com.carlosarroyoam.userservice.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.jupiter.api.Test;

import com.carlosarroyoam.userservice.dto.LoginRequest;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response.Status;

@QuarkusTest
class AuthResourceTest {

	@Test
	void shouldReturnOkWhenAuthUserWithValidCredentials() {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("carroyom");
		loginRequest.setPassword("secret");

		given().contentType(ContentType.JSON).body(loginRequest).when().post("/api/v1/auth/signin").then()
				.statusCode(Status.OK.getStatusCode()).body("username", is(not(nullValue())))
				.body("access_token", is(not(nullValue())));
	}

	@Test
	void shouldReturnUnauthorizedWhenAuthUserWithNonValidCredentials() {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setUsername("nonExist");
		loginRequest.setPassword("nonExist");

		given().contentType(ContentType.JSON).body(loginRequest).when().post("/api/v1/auth/signin").then()
				.statusCode(Status.UNAUTHORIZED.getStatusCode());
	}

}
