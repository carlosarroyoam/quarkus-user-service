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

import com.carlosarroyoam.userservice.config.AppMessages;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response.Status;

@QuarkusTest
class UserResourceTest {

	@Inject
	private AppMessages messages;

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void shouldReturnListOfUsersWhenCallFindAllEndpoint() {
		given().contentType(ContentType.JSON).when().get("/api/v1/users").then().statusCode(Status.OK.getStatusCode())
				.body("$", is(not(emptyArray()))).body("$.id", is(not(nullValue())))
				.body("$.name", is(not(nullValue()))).body("$.age", is(not(nullValue())))
				.body("$.email", is(not(nullValue()))).body("$.username", is(not(nullValue())))
				.body("$.role", is(not(nullValue()))).body("$.is_active", is(not(nullValue())))
				.body("$.created_at", is(not(nullValue()))).body("$.updated_at", is(not(nullValue())));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void shouldReturnUserWhenCallFindByIdEndpointWithExistingUserId() {
		given().contentType(ContentType.JSON).when().get("/api/v1/users/" + 1L).then()
				.statusCode(Status.OK.getStatusCode()).body("id", is(not(nullValue())))
				.body("name", is(not(nullValue()))).body("age", is(not(nullValue())))
				.body("email", is(not(nullValue()))).body("username", is(not(nullValue())))
				.body("role_id", is(not(nullValue()))).body("is_active", is(not(nullValue())))
				.body("created_at", is(not(nullValue()))).body("updated_at", is(not(nullValue())));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void shouldReturnNotFoundWhenCallFindByIdEndpointWithNonExistingUserId() {
		given().contentType(ContentType.JSON).when().get("/api/v1/users/" + 10000L).then()
				.statusCode(Status.NOT_FOUND.getStatusCode()).body("message", equalTo(messages.userNotFound()));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void shouldReturnCreatedWhenCallCreateEndpointWithValidData() {
		Map<String, Object> body = new HashMap<>();
		body.put("name", "Cathy Stefania Guido Rojas");
		body.put("email", "cguidor@mail.com");
		body.put("username", "cguidor1995");
		body.put("password", "secret");
		body.put("role_id", "User");
		body.put("age", 28);

		given().contentType(ContentType.JSON).body(body).when().post("/api/v1/users").then()
				.statusCode(Status.CREATED.getStatusCode()).headers("Location", is(not(nullValue())));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void shouldReturnBadRequestWhenCallCreateEndpointWithExistingUsername() {
		Map<String, Object> body = new HashMap<>();
		body.put("name", "Cathy Stefania Guido Rojas");
		body.put("email", "cguidor@mail.com");
		body.put("username", "carroyom");
		body.put("password", "secret");
		body.put("role_id", "User");
		body.put("age", 28);

		given().contentType(ContentType.JSON).body(body).when().post("/api/v1/users").then()
				.statusCode(Status.BAD_REQUEST.getStatusCode())
				.body("message", equalTo(messages.usernameAlreadyTaken()));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void shouldReturnBadRequestWhenCallCreateEndpointWithExistingEmail() {
		Map<String, Object> body = new HashMap<>();
		body.put("name", "Cathy Stefania Guido Rojas");
		body.put("email", "carroyom@mail.com");
		body.put("username", "cguidor");
		body.put("password", "secret");
		body.put("role_id", "User");
		body.put("age", 28);

		given().contentType(ContentType.JSON).body(body).when().post("/api/v1/users").then()
				.statusCode(Status.BAD_REQUEST.getStatusCode()).body("message", equalTo(messages.emailAlreadyTaken()));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void shouldReturnUserWhenCallUpdateEndpointWithValidData() {
		Map<String, Object> body = new HashMap<>();
		body.put("name", "Carlos Arroyo Martínez");
		body.put("age", 29);

		given().contentType(ContentType.JSON).body(body).when().patch("/api/v1/users/" + 1L).then()
				.statusCode(Status.OK.getStatusCode()).body("id", is(not(nullValue())))
				.body("name", is(not(nullValue()))).body("age", is(not(nullValue())))
				.body("email", is(not(nullValue()))).body("username", is(not(nullValue())))
				.body("role_id", is(not(nullValue()))).body("is_active", is(not(nullValue())))
				.body("created_at", is(not(nullValue()))).body("updated_at", is(not(nullValue())));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void shouldReturnNotFoundWhenCallUpdateEndpointWithNonExistingUserId() {
		Map<String, Object> body = new HashMap<>();
		body.put("name", "Carlos Arroyo Martínez");
		body.put("age", 29);

		given().contentType(ContentType.JSON).body(body).when().patch("/api/v1/users/" + 1000L).then()
				.statusCode(Status.NOT_FOUND.getStatusCode()).body("message", equalTo(messages.userNotFound()));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void shouldReturnNoContentWhenCallDeleteEndpointWithExistingUserId() {
		given().contentType(ContentType.JSON).when().delete("/api/v1/users/" + 1L).then()
				.statusCode(Status.NO_CONTENT.getStatusCode());
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void shouldReturnNotFoundWhenCallDeleteEndpointWithNonExistingUserId() {
		given().contentType(ContentType.JSON).when().delete("/api/v1/users/" + 1000L).then()
				.statusCode(Status.NOT_FOUND.getStatusCode()).body("message", equalTo(messages.userNotFound()));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void shouldReturnNoContentWhenCallChangePasswordEndpointWithValidData() {
		Map<String, Object> body = new HashMap<>();
		body.put("current_password", "secret");
		body.put("new_password", "new-secret");
		body.put("confirm_password", "new-secret");

		given().contentType(ContentType.JSON).body(body).when().post("/api/v1/users/" + 1L + "/change-password").then()
				.statusCode(Status.NO_CONTENT.getStatusCode());
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void shouldReturnBadRequestWhenCallChangePasswordEndpointWithNonValidCredentials() {
		Map<String, Object> body = new HashMap<>();
		body.put("current_password", "non-valid-secret");
		body.put("new_password", "new-secret");
		body.put("confirm_password", "new-secret");

		given().contentType(ContentType.JSON).body(body).when().post("/api/v1/users/" + 1L + "/change-password").then()
				.statusCode(Status.BAD_REQUEST.getStatusCode())
				.body("message", equalTo(messages.unauthorizedCredentials()));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void shouldReturnBadRequestWhenCallChangePasswordEndpointWithNonValidConfirmPassword() {
		Map<String, Object> body = new HashMap<>();
		body.put("current_password", "secret");
		body.put("new_password", "new-secret");
		body.put("confirm_password", "non-valid-new-secret");

		given().contentType(ContentType.JSON).body(body).when().post("/api/v1/users/" + 1L + "/change-password").then()
				.statusCode(Status.BAD_REQUEST.getStatusCode())
				.body("message", equalTo(messages.passwordsDoesntMatch()));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = { "Admin", "User" })
	void shouldReturnUserWhenCallUsersSelfEndpoint() {
		given().contentType(ContentType.JSON).when().get("/api/v1/users/self").then()
				.statusCode(Status.OK.getStatusCode()).body("id", is(not(nullValue())))
				.body("name", is(not(nullValue()))).body("age", is(not(nullValue())))
				.body("email", is(not(nullValue()))).body("username", is(not(nullValue())))
				.body("role", is(not(nullValue()))).body("is_active", is(not(nullValue())))
				.body("created_at", is(not(nullValue()))).body("updated_at", is(not(nullValue())));
	}

}
