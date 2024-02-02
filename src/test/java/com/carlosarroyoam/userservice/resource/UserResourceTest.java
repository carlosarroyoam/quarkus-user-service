package com.carlosarroyoam.userservice.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.emptyArray;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.carlosarroyoam.userservice.constant.AppMessages;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response.Status;

@QuarkusTest
class UserResourceTest {

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void testFindAllEndpoint() {
		given().contentType(ContentType.JSON).when().get("/api/v1/users").then().statusCode(Status.OK.getStatusCode())
				.body("$", is(not(emptyArray()))).body("$.id", is(not(nullValue())))
				.body("$.name", is(not(nullValue()))).body("$.age", is(not(nullValue())))
				.body("$.email", is(not(nullValue()))).body("$.username", is(not(nullValue())))
				.body("$.role", is(not(nullValue()))).body("$.is_active", is(not(nullValue())))
				.body("$.created_at", is(not(nullValue()))).body("$.updated_at", is(not(nullValue())));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void testFindByIdEndpointWithExistingUser() {
		given().contentType(ContentType.JSON).when().get("/api/v1/users/" + 1L).then()
				.statusCode(Status.OK.getStatusCode()).body("id", is(not(nullValue())))
				.body("name", is(not(nullValue()))).body("age", is(not(nullValue())))
				.body("email", is(not(nullValue()))).body("username", is(not(nullValue())))
				.body("role", is(not(nullValue()))).body("is_active", is(not(nullValue())))
				.body("created_at", is(not(nullValue()))).body("updated_at", is(not(nullValue())));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void testFindByIdEndpointWithNonExistingUser() {
		given().contentType(ContentType.JSON).when().get("/api/v1/users/" + 10000L).then()
				.statusCode(Status.NOT_FOUND.getStatusCode())
				.body("message", equalTo(AppMessages.USER_ID_NOT_FOUND_EXCEPTION_MESSAGE));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void testCreateEndpoint() {
		Map<String, Object> body = new HashMap<>();
		body.put("name", "Cathy Stefania Guido Rojas");
		body.put("email", "cguidor@mail.com");
		body.put("username", "cguidor");
		body.put("password", "secret");
		body.put("role", "User");
		body.put("age", 28);

		given().contentType(ContentType.JSON).body(body).when().post("/api/v1/users").then()
				.statusCode(Status.CREATED.getStatusCode()).headers("Location", is(not(nullValue())));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void testCreateEndpointWithExistingUsername() {
		Map<String, Object> body = new HashMap<>();
		body.put("name", "Cathy Stefania Guido Rojas");
		body.put("email", "cguidor@mail.com");
		body.put("username", "carroyom");
		body.put("password", "secret");
		body.put("role", "User");
		body.put("age", 28);

		given().contentType(ContentType.JSON).body(body).when().post("/api/v1/users").then()
				.statusCode(Status.BAD_REQUEST.getStatusCode())
				.body("message", equalTo(AppMessages.USERNAME_ALREADY_EXISTS_EXCEPTION_MESSAGE));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void testCreateEndpointWithExistingMail() {
		Map<String, Object> body = new HashMap<>();
		body.put("name", "Cathy Stefania Guido Rojas");
		body.put("email", "carroyom@mail.com");
		body.put("username", "cguidor");
		body.put("password", "secret");
		body.put("role", "User");
		body.put("age", 28);

		given().contentType(ContentType.JSON).body(body).when().post("/api/v1/users").then()
				.statusCode(Status.BAD_REQUEST.getStatusCode())
				.body("message", equalTo(AppMessages.EMAIL_ALREADY_EXISTS_EXCEPTION_MESSAGE));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void testUpdateEndpoint() {
		Map<String, Object> body = new HashMap<>();
		body.put("name", "Carlos Arroyo Martínez");
		body.put("age", 29);

		given().contentType(ContentType.JSON).body(body).when().patch("/api/v1/users/" + 1L).then()
				.statusCode(Status.OK.getStatusCode()).body("id", is(not(nullValue())))
				.body("name", is(not(nullValue()))).body("age", is(not(nullValue())))
				.body("email", is(not(nullValue()))).body("username", is(not(nullValue())))
				.body("role", is(not(nullValue()))).body("is_active", is(not(nullValue())))
				.body("created_at", is(not(nullValue()))).body("updated_at", is(not(nullValue())));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void testUpdateEndpointWithNonExistingUser() {
		Map<String, Object> body = new HashMap<>();
		body.put("name", "Carlos Arroyo Martínez");
		body.put("age", 29);

		given().contentType(ContentType.JSON).body(body).when().patch("/api/v1/users/" + 1000L).then()
				.statusCode(Status.NOT_FOUND.getStatusCode())
				.body("message", equalTo(AppMessages.USER_ID_NOT_FOUND_EXCEPTION_MESSAGE));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void testDeleteEndpoint() {
		given().contentType(ContentType.JSON).when().delete("/api/v1/users/" + 1L).then()
				.statusCode(Status.NO_CONTENT.getStatusCode());
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void testDeleteEndpointWithNonExistingUser() {
		given().contentType(ContentType.JSON).when().delete("/api/v1/users/" + 1000L).then()
				.statusCode(Status.NOT_FOUND.getStatusCode())
				.body("message", equalTo(AppMessages.USER_ID_NOT_FOUND_EXCEPTION_MESSAGE));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void testChangePasswordEndpoint() {
		Map<String, Object> body = new HashMap<>();
		body.put("current_password", "secret");
		body.put("new_password", "new-secret");
		body.put("confirm_password", "new-secret");

		given().contentType(ContentType.JSON).body(body).when().post("/api/v1/users/" + 1L + "/change-password").then()
				.statusCode(Status.OK.getStatusCode());
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void testChangePasswordEndpointWithNonValidCredentials() {
		Map<String, Object> body = new HashMap<>();
		body.put("current_password", "non-valid-secret");
		body.put("new_password", "new-secret");
		body.put("confirm_password", "new-secret");

		given().contentType(ContentType.JSON).body(body).when().post("/api/v1/users/" + 1L + "/change-password").then()
				.statusCode(Status.BAD_REQUEST.getStatusCode())
				.body("message", equalTo(AppMessages.UNAUTHORIZED_CREDENTIALS_EXCEPTION_MESSAGE));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void testChangePasswordEndpointWithNonValidConfirmPassword() {
		Map<String, Object> body = new HashMap<>();
		body.put("current_password", "new-secret");
		body.put("new_password", "new-secret");
		body.put("confirm_password", "non-valid-new-secret");

		given().contentType(ContentType.JSON).body(body).when().post("/api/v1/users/" + 1L + "/change-password").then()
				.statusCode(Status.BAD_REQUEST.getStatusCode())
				.body("message", equalTo(AppMessages.PASSWORDS_NOT_MATCH_EXCEPTION_DETAILED_MESSAGE));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void testMeEndpoint() {
		given().contentType(ContentType.JSON).when().get("/api/v1/users/me").then()
				.statusCode(Status.OK.getStatusCode()).body("id", is(not(nullValue())))
				.body("name", is(not(nullValue()))).body("age", is(not(nullValue())))
				.body("email", is(not(nullValue()))).body("username", is(not(nullValue())))
				.body("role", is(not(nullValue()))).body("is_active", is(not(nullValue())))
				.body("created_at", is(not(nullValue()))).body("updated_at", is(not(nullValue())));
	}

}
