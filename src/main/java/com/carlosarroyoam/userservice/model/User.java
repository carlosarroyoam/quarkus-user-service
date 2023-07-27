package com.carlosarroyoam.userservice.model;

import java.io.Serializable;
import java.time.ZonedDateTime;

import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@UserDefinition
@Data
public class User implements Serializable {

	private static final long serialVersionUID = 4866188536792947164L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String name;

	@Column
	private Integer age;

	@Column(nullable = false, unique = true)
	private String mail;

	@Column(nullable = false, unique = true)
	@Username
	private String username;

	@Column(nullable = false)
	@Password
	private String password;

	@Column(nullable = false)
	@Roles
	private String role;

	@Column(nullable = false)
	private Boolean isActive;

	@Column(nullable = false)
	private ZonedDateTime createdAt;

	@Column(nullable = false)
	private ZonedDateTime updatedAt;

}
