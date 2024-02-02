package com.carlosarroyoam.userservice.constant;

public class AppMessages {

	public static final String USER_ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE = "User account not found";
	public static final String USER_ACCOUNT_NOT_FOUND_EXCEPTION_DETAILED_MESSAGE = "User account with username: '%s' was not found in this server";

	public static final String USER_ACCOUNT_NOT_ACTIVE_EXCEPTION_MESSAGE = "User account is not active";
	public static final String USER_ACCOUNT_NOT_ACTIVE_EXCEPTION_DETAILED_MESSAGE = "User account with username: '%s' is not active";

	public static final String UNAUTHORIZED_CREDENTIALS_EXCEPTION_MESSAGE = "Incorrect username or password";
	public static final String UNAUTHORIZED_CREDENTIALS_EXCEPTION_DETAILED_MESSAGE = "User account with username: '%s' failed to authenticate due to incorrect username or password";

	public static final String USER_ID_NOT_FOUND_EXCEPTION_MESSAGE = "User not found";
	public static final String USER_ID_NOT_FOUND_EXCEPTION_DETAILED_MESSAGE = "User with id: '%d' was not found";

	public static final String USER_USERNAME_NOT_FOUND_EXCEPTION_MESSAGE = "User not found";
	public static final String USER_USERNAME_NOT_FOUND_EXCEPTION_DETAILED_MESSAGE = "User with username: '%s' was not found";

	public static final String USERNAME_ALREADY_EXISTS_EXCEPTION_MESSAGE = "Username already exists";
	public static final String USERNAME_ALREADY_EXISTS_EXCEPTION_DETAILED_MESSAGE = "User with username: %s already exists";

	public static final String EMAIL_ALREADY_EXISTS_EXCEPTION_MESSAGE = "Email already exists";
	public static final String EMAIL_ALREADY_EXISTS_EXCEPTION_DETAILED_MESSAGE = "Email with address: %s already exists";

	public static final String PASSWORDS_NOT_MATCH_EXCEPTION_MESSAGE = "Passwords doesn't match";
	public static final String PASSWORDS_NOT_MATCH_EXCEPTION_DETAILED_MESSAGE = "Passwords doesn't match";

	private AppMessages() {
		throw new IllegalAccessError(AppConstants.STANDARD_ILLEGAL_ACCESS_EXCEPTION_MESSAGE_UTILITY_CLASS);
	}

}
