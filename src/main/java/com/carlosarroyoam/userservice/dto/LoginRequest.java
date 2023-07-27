package com.carlosarroyoam.userservice.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest implements Serializable {

	private static final long serialVersionUID = -4381974388727561283L;

	@NotBlank(message = "Username should not be blank")
	private String username;

	@NotBlank(message = "Password should not be blank")
	private String password;

}
