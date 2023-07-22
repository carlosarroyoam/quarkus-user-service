package com.carlosarroyoam.userservice.constant;

public class AppConstants {

	public static final String STANDARD_ILLEGAL_ACCESS_EXCEPTION_MESSAGE_UTILITY_CLASS = "Illegal access to utility class";

	public static final String JWT_ISSUER_PROPERTY = "com.carlosarroyoam.jwt.sing.issuer";

	private AppConstants() {
		throw new IllegalAccessError(STANDARD_ILLEGAL_ACCESS_EXCEPTION_MESSAGE_UTILITY_CLASS);
	}

}
