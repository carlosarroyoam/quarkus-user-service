package com.carlosarroyoam.userservice.resources;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.jupiter.api.Test;

import com.carlosarroyoam.userservice.constants.AppMessages;
import com.carlosarroyoam.userservice.model.User;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response.Status;

@QuarkusTest
class UserResourceTest {

	@Test
	@TestSecurity(user = "carroyom", roles = {"Admin", "Urser"})
	void testFindAllEndpoint() {
		given()
		.contentType(ContentType.JSON)
		.when().get("/users")
		.then()
		.statusCode(Status.OK.getStatusCode());
	}

	@Test
	@TestSecurity(user = "carroyom", roles = {"Admin", "Urser"})
	void testFindByIdEndpointWithExistingUser() {
		given()
		.contentType(ContentType.JSON)
		.when().get("/users/" + 1)
		.then()
		.statusCode(Status.OK.getStatusCode())
		.body("id", equalTo(1))
		.body("username", equalTo("carroyom"));
	}
	
	@Test
	@TestSecurity(user = "carroyom", roles = {"Admin", "Urser"})
	void testFindByIdEndpointWithNonExistingUser() {
		given()
		.contentType(ContentType.JSON)
		.when().get("/users/" + 10000)
		.then()
		.statusCode(Status.NOT_FOUND.getStatusCode());
	}

	@Test
	@TestSecurity(user = "carroyom", roles = {"Admin", "Urser"})
	void testCreateEndpoint() {
		User user = new User();
		user.setName("Cathy Stefania Guido Rojas");
		user.setMail("cguidor2@mail.com");
		user.setUsername("cguidor2");
		user.setPassword("secret");
		user.setRole("User");
		user.setAge(28);
		
		given()
		.contentType(ContentType.JSON)
		.body(user)
		.when().post("/users")
		.then()
		.statusCode(Status.CREATED.getStatusCode())
		.headers("Location", is(not(nullValue())));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = {"Admin", "Urser"})
	void testCreateEndpointWithExistingUsername() {
		User user = new User();
		user.setName("Cathy Stefania Guido Rojas");
		user.setMail("cguidor@mail.com");
		user.setUsername("carroyom");
		user.setPassword("secret");
		user.setRole("User");
		user.setAge(28);
		
		given()
		.contentType(ContentType.JSON)
		.body(user)
		.when().post("/users")
		.then()
		.statusCode(Status.BAD_REQUEST.getStatusCode())
		.body("message", equalTo(AppMessages.USERNAME_ALREADY_EXISTS_EXCEPTION_MESSAGE));
	}
	
	@Test
	@TestSecurity(user = "carroyom", roles = {"Admin", "Urser"})
	void testCreateEndpointWithExistingMail() {
		User user = new User();
		user.setName("Cathy Stefania Guido Rojas");
		user.setMail("carroyom@mail.com");
		user.setUsername("cguidor");
		user.setPassword("secret");
		user.setRole("User");
		user.setAge(28);
		
		given()
		.contentType(ContentType.JSON)
		.body(user)
		.when().post("/users")
		.then()
		.statusCode(Status.BAD_REQUEST.getStatusCode())
		.body("message", equalTo(AppMessages.MAIL_ALREADY_EXISTS_EXCEPTION_MESSAGE));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = {"Admin", "Urser"})
	void testMeEndpoint() {
		given()
		.contentType(ContentType.JSON)
		.when().get("/users/me")
		.then()
		.statusCode(Status.OK.getStatusCode())
		.body("id", equalTo(1))
		.body("username", equalTo("carroyom"));
	}
}
