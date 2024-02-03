package com.carlosarroyoam.userservice.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordRequest implements Serializable {

	private static final long serialVersionUID = -8023584346199877536L;

	@NotBlank(message = "Current password should not be blank")
	private String currentPassword;

	@NotBlank(message = "New password should not be blank")
	private String newPassword;

	@NotBlank(message = "Confirm password should not be blank")
	private String confirmPassword;

}
