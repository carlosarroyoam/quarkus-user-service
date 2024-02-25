package com.carlosarroyoam.userservice.model;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

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

	@Column(name = "is_active", nullable = false)
	private Boolean isActive;

	@Column(name = "role_id", nullable = false)
	private Integer roleId;

	@ManyToOne
	@JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Role role;

	@Column(name = "created_at", nullable = false)
	private ZonedDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private ZonedDateTime updatedAt;

}
