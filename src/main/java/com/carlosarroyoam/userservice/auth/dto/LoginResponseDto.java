package com.carlosarroyoam.userservice.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/** Representa el resultado de una autenticación exitosa: nombre de usuario y token de acceso. */
@Getter
@Setter
@Builder
public class LoginResponseDto {
  private String username;
  private String accessToken;
}
