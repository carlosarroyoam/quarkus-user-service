package com.carlosarroyoam.userservice.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserDto implements Serializable {

	private static final long serialVersionUID = 3245759926280364595L;

	@NotBlank(message = "Name should not be blank")
	private String name;

	@Min(message = "Age should be min 18", value = 18)
	@Max(message = "Age should be max 100", value = 100)
	private Integer age;

	@Email(message = "Email should be an valid email address")
	private String email;

	@NotBlank(message = "Username should not be blank")
	private String username;

	@NotBlank(message = "Password should not be blank")
	private String password;

	@NotBlank(message = "Role should not be blank")
	private String role;

}
