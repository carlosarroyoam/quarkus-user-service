package com.carlosarroyoam.userservice.constants;

public class AppMessages {

	public static final String USER_NOT_FOUND_EXCEPTION_MESSAGE = "User not found";
	public static final String USER_NOT_FOUND_EXCEPTION_DETAILED_MESSAGE = "User account with username: '%s' was not found in this server";

	public static final String USER_NOT_ACTIVE_EXCEPTION_MESSAGE = "User is not active";
	public static final String USER_NOT_ACTIVE_EXCEPTION_DETAILED_MESSAGE = "User account with username: '%s' is not active";

	public static final String UNAUTHORIZED_CREDENTIALS_EXCEPTION_MESSAGE = "Incorrect username or password";
	public static final String UNAUTHORIZED_CREDENTIALS_EXCEPTION_DETAILED_MESSAGE = "User account with username: '%s' failed to authenticate due to incorrect username or password";

	private AppMessages() {
		throw new IllegalAccessError(AppConstants.STANDARD_MESSAGE_UTILITY_CLASS);
	}

}
