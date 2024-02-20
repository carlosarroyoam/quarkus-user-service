package com.carlosarroyoam.userservice.model;

import java.io.Serializable;
import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User implements Serializable {

	private static final long serialVersionUID = 4866188536792947164L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name", length = 128)
	private String name;

	@Column(name = "age")
	private Byte age;

	@Column(name = "email", length = 128, nullable = false, unique = true)
	private String email;

	@Column(name = "username", length = 128, nullable = false, unique = true)
	private String username;

	@Column(name = "password", length = 128, nullable = false)
	private String password;

	@Column(name = "role", length = 32, nullable = false)
	private String role;

	@Column(name = "is_active", nullable = false)
	private Boolean isActive;

	@Column(name = "created_at", nullable = false)
	private ZonedDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private ZonedDateTime updatedAt;

}
