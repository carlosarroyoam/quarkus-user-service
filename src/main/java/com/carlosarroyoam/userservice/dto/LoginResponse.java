package com.carlosarroyoam.userservice.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class LoginResponse implements Serializable {

	private static final long serialVersionUID = -2458089868824197334L;
	private String username;
	private String accessToken;

}
