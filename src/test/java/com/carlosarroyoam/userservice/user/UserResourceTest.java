package com.carlosarroyoam.userservice.user;

import com.carlosarroyoam.userservice.core.config.AppMessages;
import com.carlosarroyoam.userservice.user.dto.ChangePasswordRequestDto;
import com.carlosarroyoam.userservice.user.dto.CreateUserRequestDto;
import com.carlosarroyoam.userservice.user.dto.UpdateUserRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.emptyArray;

@QuarkusTest
class UserResourceTest {
  @Inject
  private AppMessages messages;

  @Inject
  private ObjectMapper mapper;

  @BeforeEach
  void setUp() {
    RestAssured.config = RestAssured.config()
        .objectMapperConfig(
            new ObjectMapperConfig().jackson2ObjectMapperFactory((cls, charset) -> mapper));
  }

  @Test
  @TestSecurity(user = "carroyom", roles = { "Admin", "User" })
  void shouldReturnListOfUsersWhenCallFindAllEndpoint() {
    given().contentType(ContentType.JSON)
        .when()
        .get("/api/v1/users")
        .then()
        .statusCode(Status.OK.getStatusCode())
        .body("$", is(not(emptyArray())))
        .body("$.id", is(not(nullValue())))
        .body("$.name", is(not(nullValue())))
        .body("$.age", is(not(nullValue())))
        .body("$.email", is(not(nullValue())))
        .body("$.username", is(not(nullValue())))
        .body("$.role_id", is(not(nullValue())))
        .body("$.is_active", is(not(nullValue())))
        .body("$.created_at", is(not(nullValue())))
        .body("$.updated_at", is(not(nullValue())));
  }

  @Test
  @TestSecurity(user = "carroyom", roles = { "Admin", "User" })
  void shouldReturnUserWhenCallFindByIdEndpointWithExistingUserId() {
    given().contentType(ContentType.JSON)
        .pathParam("userId", 1L)
        .when()
        .get("/api/v1/users/{userId}")
        .then()
        .statusCode(Status.OK.getStatusCode())
        .body("id", is(not(nullValue())))
        .body("name", is(not(nullValue())))
        .body("age", is(not(nullValue())))
        .body("email", is(not(nullValue())))
        .body("username", is(not(nullValue())))
        .body("role_id", is(not(nullValue())))
        .body("is_active", is(not(nullValue())))
        .body("created_at", is(not(nullValue())))
        .body("updated_at", is(not(nullValue())));
  }

  @Test
  @TestSecurity(user = "carroyom", roles = { "Admin", "User" })
  void shouldReturnNotFoundWhenCallFindByIdEndpointWithNonExistingUserId() {
    given().contentType(ContentType.JSON)
        .pathParam("userId", 10000L)
        .when()
        .get("/api/v1/users/{userId}")
        .then()
        .statusCode(Status.NOT_FOUND.getStatusCode())
        .body("message", equalTo(messages.userNotFound()));
  }

  @Test
  @TestSecurity(user = "carroyom", roles = { "Admin", "User" })
  void shouldReturnCreatedWhenCallCreateEndpointWithValidData() {
    CreateUserRequestDto requestDto = CreateUserRequestDto.builder()
        .name("Cathy Stefania Guido Rojas")
        .email("cguidor@mail.com")
        .username("cguidor1995")
        .password("secret")
        .roleId(2)
        .age(Byte.valueOf("28"))
        .build();

    given().contentType(ContentType.JSON)
        .body(requestDto)
        .when()
        .post("/api/v1/users")
        .then()
        .statusCode(Status.CREATED.getStatusCode())
        .headers("Location", is(not(nullValue())));
  }

  @Test
  @TestSecurity(user = "carroyom", roles = { "Admin", "User" })
  void shouldReturnBadRequestWhenCallCreateEndpointWithExistingUsername() {
    CreateUserRequestDto requestDto = CreateUserRequestDto.builder()
        .name("Cathy Stefania Guido Rojas")
        .email("cguidor@mail.com")
        .username("carroyom")
        .password("secret")
        .roleId(2)
        .age(Byte.valueOf("28"))
        .build();

    given().contentType(ContentType.JSON)

        .body(requestDto)
        .when()
        .post("/api/v1/users")
        .then()
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body("message", equalTo(messages.usernameAlreadyTaken()));
  }

  @Test
  @TestSecurity(user = "carroyom", roles = { "Admin", "User" })
  void shouldReturnBadRequestWhenCallCreateEndpointWithExistingEmail() {
    CreateUserRequestDto requestDto = CreateUserRequestDto.builder()
        .name("Cathy Stefania Guido Rojas")
        .email("carroyom@mail.com")
        .username("cguidor")
        .password("secret")
        .roleId(2)
        .age(Byte.valueOf("28"))
        .build();

    given().contentType(ContentType.JSON)

        .body(requestDto)
        .when()
        .post("/api/v1/users")
        .then()
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body("message", equalTo(messages.emailAlreadyTaken()));
  }

  @Test
  @TestSecurity(user = "carroyom", roles = { "Admin", "User" })
  void shouldReturnUserWhenCallUpdateEndpointWithValidData() {
    UpdateUserRequestDto requestDto = UpdateUserRequestDto.builder()
        .name("Carlos Arroyo Martínez")
        .age(Byte.valueOf("29"))
        .build();

    given().contentType(ContentType.JSON)
        .pathParam("userId", 1L)
        .body(requestDto)
        .when()
        .put("/api/v1/users/{userId}")
        .then()
        .statusCode(Status.NO_CONTENT.getStatusCode());
  }

  @Test
  @TestSecurity(user = "carroyom", roles = { "Admin", "User" })
  void shouldReturnNotFoundWhenCallUpdateEndpointWithNonExistingUserId() {
    UpdateUserRequestDto requestDto = UpdateUserRequestDto.builder()
        .name("Carlos Arroyo Martínez")
        .age(Byte.valueOf("29"))
        .build();

    given().contentType(ContentType.JSON)
        .pathParam("userId", 1000L)
        .body(requestDto)
        .when()
        .put("/api/v1/users/{userId}")
        .then()
        .statusCode(Status.NOT_FOUND.getStatusCode())
        .body("message", equalTo(messages.userNotFound()));
  }

  @Test
  @TestSecurity(user = "carroyom", roles = { "Admin", "User" })
  void shouldReturnNoContentWhenCallDeleteEndpointWithExistingUserId() {
    given().contentType(ContentType.JSON)
        .pathParam("userId", 1L)
        .when()
        .delete("/api/v1/users/{userId}")
        .then()
        .statusCode(Status.NO_CONTENT.getStatusCode());
  }

  @Test
  @TestSecurity(user = "carroyom", roles = { "Admin", "User" })
  void shouldReturnNotFoundWhenCallDeleteEndpointWithNonExistingUserId() {
    given().contentType(ContentType.JSON)
        .pathParam("userId", 1000L)
        .when()
        .delete("/api/v1/users/{userId}")
        .then()
        .statusCode(Status.NOT_FOUND.getStatusCode())
        .body("message", equalTo(messages.userNotFound()));
  }

  @Test
  @TestSecurity(user = "carroyom", roles = { "Admin", "User" })
  void shouldReturnNoContentWhenCallChangePasswordEndpointWithValidData() {
    ChangePasswordRequestDto requestDto = ChangePasswordRequestDto.builder()
        .currentPassword("secret")
        .newPassword("new-secret")
        .confirmPassword("new-secret")
        .build();

    given().contentType(ContentType.JSON)
        .pathParam("userId", 1L)
        .body(requestDto)
        .when()
        .post("/api/v1/users/{userId}/change-password", 1L)
        .then()
        .statusCode(Status.NO_CONTENT.getStatusCode());
  }

  @Test
  @TestSecurity(user = "carroyom", roles = { "Admin", "User" })
  void shouldReturnBadRequestWhenCallChangePasswordEndpointWithNonValidCredentials() {
    ChangePasswordRequestDto requestDto = ChangePasswordRequestDto.builder()
        .currentPassword("non-valid-secret")
        .newPassword("new-secret")
        .confirmPassword("new-secret")
        .build();

    given().contentType(ContentType.JSON)
        .pathParam("userId", 1L)
        .body(requestDto)
        .when()
        .post("/api/v1/users/{userId}/change-password")
        .then()
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body("message", equalTo(messages.unauthorizedCredentials()));
  }

  @Test
  @TestSecurity(user = "carroyom", roles = { "Admin", "User" })
  void shouldReturnBadRequestWhenCallChangePasswordEndpointWithNonValidConfirmPassword() {
    ChangePasswordRequestDto requestDto = ChangePasswordRequestDto.builder()
        .currentPassword("secret")
        .newPassword("new-secret")
        .confirmPassword("non-valid-new-secret")
        .build();

    given().contentType(ContentType.JSON)
        .pathParam("userId", 1L)
        .body(requestDto)
        .when()
        .post("/api/v1/users/{userId}/change-password")
        .then()
        .statusCode(Status.BAD_REQUEST.getStatusCode())
        .body("message", equalTo(messages.passwordsDoesntMatch()));
  }

  @Test
  @TestSecurity(user = "carroyom", roles = { "Admin", "User" })
  void shouldReturnUserWhenCallUsersSelfEndpoint() {
    given().contentType(ContentType.JSON)
        .when()
        .get("/api/v1/users/self")
        .then()
        .statusCode(Status.OK.getStatusCode())
        .body("id", is(not(nullValue())))
        .body("name", is(not(nullValue())))
        .body("age", is(not(nullValue())))
        .body("email", is(not(nullValue())))
        .body("username", is(not(nullValue())))
        .body("role_id", is(not(nullValue())))
        .body("is_active", is(not(nullValue())))
        .body("created_at", is(not(nullValue())))
        .body("updated_at", is(not(nullValue())));
  }
}
