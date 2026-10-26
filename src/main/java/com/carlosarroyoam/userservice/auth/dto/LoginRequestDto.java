package com.carlosarroyoam.userservice.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/** Representa las credenciales enviadas para autenticar a un usuario. */
@Getter
@Setter
@Builder
public class LoginRequestDto {
  @NotBlank(message = "Username should not be blank")
  private String username;

  @NotBlank(message = "Password should not be blank")
  private String password;
}
