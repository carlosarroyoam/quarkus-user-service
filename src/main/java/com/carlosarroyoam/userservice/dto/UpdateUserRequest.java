package com.carlosarroyoam.userservice.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class UpdateUserRequest implements Serializable {

	private static final long serialVersionUID = 2930409985629656477L;

	private String name;
	private Integer age;

}
