package com.carlosarroyoam.userservice.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class UserResponse implements Serializable {

	private static final long serialVersionUID = -6935255287896729891L;
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
