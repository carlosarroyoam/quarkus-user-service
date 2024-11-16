package com.carlosarroyoam.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
	@NotBlank(message = "Username should not be blank")
	private String username;

	@NotBlank(message = "Password should not be blank")
	private String password;
}
