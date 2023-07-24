package com.carlosarroyoam.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class CreateUserDto {

	@NotBlank(message = "Name should not be blank")
	private String name;

	@Min(message = "Age should be min 18", value = 18)
	@Max(message = "Age should be max 100", value = 100)
	private Integer age;

	@Email(message = "Mail should be an valid email address")
	private String mail;

	@NotBlank(message = "Username should not be blank")
	private String username;

	@NotBlank(message = "Password should not be blank")
	private String password;

	@NotBlank(message = "Role should not be blank")
	private String role;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}
