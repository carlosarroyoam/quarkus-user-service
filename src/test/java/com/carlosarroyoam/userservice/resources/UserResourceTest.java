package com.carlosarroyoam.userservice.resources;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.carlosarroyoam.userservice.constants.AppMessages;
import com.carlosarroyoam.userservice.dto.CreateUserDto;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response.Status;

@QuarkusTest
@Tag("integration")
@TestTransaction
class UserResourceTest {

	@Test
	@TestSecurity(user = "carroyom", roles = {"Admin", "User"})
	void testFindAllEndpoint() {
		given()
		.contentType(ContentType.JSON)
		.when().get("/api/users")
		.then()
		.statusCode(Status.OK.getStatusCode());
	}

	@Test
	@TestSecurity(user = "carroyom", roles = {"Admin", "User"})
	void testFindByIdEndpointWithExistingUser() {
		given()
		.contentType(ContentType.JSON)
		.when().get("/api/users/" + 1)
		.then()
		.statusCode(Status.OK.getStatusCode())
		.body("id", equalTo(1))
		.body("username", equalTo("carroyom"));
	}
	
	@Test
	@TestSecurity(user = "carroyom", roles = {"Admin", "User"})
	void testFindByIdEndpointWithNonExistingUser() {
		given()
		.contentType(ContentType.JSON)
		.when().get("/api/users/" + 10000)
		.then()
		.statusCode(Status.NOT_FOUND.getStatusCode());
	}

	@Test
	@TestSecurity(user = "carroyom", roles = {"Admin", "User"})
	void testCreateEndpoint() {
		CreateUserDto createUserDto = new CreateUserDto();
		createUserDto.setName("Cathy Stefania Guido Rojas");
		createUserDto.setMail("cguidor@mail.com");
		createUserDto.setUsername("cguidor");
		createUserDto.setPassword("secret");
		createUserDto.setRole("User");
		createUserDto.setAge(28);

		given()
		.contentType(ContentType.JSON)
		.body(createUserDto)
		.when().post("/api/users")
		.then()
		.statusCode(Status.CREATED.getStatusCode())
		.headers("Location", is(not(nullValue())));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = {"Admin", "User"})
	void testCreateEndpointWithExistingUsername() {
		CreateUserDto createUserDto = new CreateUserDto();
		createUserDto.setName("Cathy Stefania Guido Rojas");
		createUserDto.setMail("cguidor@mail.com");
		createUserDto.setUsername("carroyom");
		createUserDto.setPassword("secret");
		createUserDto.setRole("User");
		createUserDto.setAge(28);
		
		given()
		.contentType(ContentType.JSON)
		.body(createUserDto)
		.when().post("/api/users")
		.then()
		.statusCode(Status.BAD_REQUEST.getStatusCode())
		.body("message", equalTo(AppMessages.USERNAME_ALREADY_EXISTS_EXCEPTION_MESSAGE));
	}
	
	@Test
	@TestSecurity(user = "carroyom", roles = {"Admin", "User"})
	void testCreateEndpointWithExistingMail() {
		CreateUserDto createUserDto = new CreateUserDto();
		createUserDto.setName("Cathy Stefania Guido Rojas");
		createUserDto.setMail("carroyom@mail.com");
		createUserDto.setUsername("cguidor");
		createUserDto.setPassword("secret");
		createUserDto.setRole("User");
		createUserDto.setAge(28);
		
		given()
		.contentType(ContentType.JSON)
		.body(createUserDto)
		.when().post("/api/users")
		.then()
		.statusCode(Status.BAD_REQUEST.getStatusCode())
		.body("message", equalTo(AppMessages.MAIL_ALREADY_EXISTS_EXCEPTION_MESSAGE));
	}

	@Test
	@TestSecurity(user = "carroyom", roles = {"Admin", "User"})
	void testMeEndpoint() {
		given()
		.contentType(ContentType.JSON)
		.when().get("/api/users/me")
		.then()
		.statusCode(Status.OK.getStatusCode())
		.body("id", equalTo(1))
		.body("username", equalTo("carroyom"));
	}

}
