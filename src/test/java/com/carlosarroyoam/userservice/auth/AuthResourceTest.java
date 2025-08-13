package com.carlosarroyoam.userservice.auth;

import com.carlosarroyoam.userservice.auth.dto.LoginRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response.Status;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

@QuarkusTest
class AuthResourceTest {
  @Test
  void shouldReturnOkWhenAuthUserWithValidCredentials() throws JsonProcessingException {
    LoginRequestDto requestDto = LoginRequestDto.builder()
        .username("carroyom")
        .password("secret")
        .build();

    given().contentType(ContentType.JSON)
        .body(requestDto)
        .when()
        .post("/api/v1/auth/signin")
        .then()
        .statusCode(Status.OK.getStatusCode())
        .body("username", is(not(nullValue())))
        .body("access_token", is(not(nullValue())));
  }

  @Test
  void shouldReturnUnauthorizedWhenAuthUserWithNonValidCredentials() {
    LoginRequestDto requestDto = LoginRequestDto.builder()
        .username("nonExist")
        .password("nonExist")
        .build();

    given().contentType(ContentType.JSON)
        .body(requestDto)
        .when()
        .post("/api/v1/auth/signin")
        .then()
        .statusCode(Status.UNAUTHORIZED.getStatusCode());
  }
}
