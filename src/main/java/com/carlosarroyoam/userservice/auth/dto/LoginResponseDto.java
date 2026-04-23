package com.carlosarroyoam.userservice.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponseDto {
  private String username;
  private String accessToken;
}
