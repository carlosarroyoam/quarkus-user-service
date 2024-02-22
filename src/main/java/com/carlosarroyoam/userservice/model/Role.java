package com.carlosarroyoam.userservice.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role implements Serializable {

	private static final long serialVersionUID = 4866188536792947164L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title", length = 32, nullable = false)
	private String title;

	@Column(name = "description", length = 128, nullable = false)
	private String description;

	@OneToMany(mappedBy = "role")
	private List<User> users = new ArrayList<>();

}
