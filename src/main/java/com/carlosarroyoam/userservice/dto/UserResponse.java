package com.carlosarroyoam.userservice.dto;

import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class UserResponse {

	private Long id;
	private String name;
	private Byte age;
	private String email;
	private String username;
	private Boolean isActive;
	private Long roleId;
	private ZonedDateTime createdAt;
	private ZonedDateTime updatedAt;

}
