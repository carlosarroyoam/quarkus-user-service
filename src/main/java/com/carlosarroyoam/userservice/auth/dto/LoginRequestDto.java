package com.carlosarroyoam.userservice.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginRequestDto {
  @NotBlank(message = "Username should not be blank")
  private String username;

  @NotBlank(message = "Password should not be blank")
  private String password;
}
