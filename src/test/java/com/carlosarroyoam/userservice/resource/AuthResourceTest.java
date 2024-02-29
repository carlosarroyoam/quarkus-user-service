package com.carlosarroyoam.userservice.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response.Status;

@QuarkusTest
class AuthResourceTest {

	@Test
	void shouldReturnOkWhenAuthUserWithValidCredentials() throws JsonProcessingException {
		Map<String, Object> body = new HashMap<>();
		body.put("username", "carroyom");
		body.put("password", "secret");

		given().contentType(ContentType.JSON).body(body).when().post("/api/v1/auth/signin").then()
				.statusCode(Status.OK.getStatusCode()).body("username", is(not(nullValue())))
				.body("access_token", is(not(nullValue())));
	}

	@Test
	void shouldReturnUnauthorizedWhenAuthUserWithNonValidCredentials() {
		Map<String, Object> body = new HashMap<>();
		body.put("username", "nonExist");
		body.put("password", "nonExist");

		given().contentType(ContentType.JSON).body(body).when().post("/api/v1/auth/signin").then()
				.statusCode(Status.UNAUTHORIZED.getStatusCode());
	}

}
