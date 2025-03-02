package com.carlosarroyoam.userservice.constant;

public class AppConstants {
  public static final String ILLEGAL_ACCESS_EXCEPTION = "Illegal access to utility class";
  public static final String JWT_ISSUER_PROPERTY = "com.carlosarroyoam.jwt.sing.issuer";

  private AppConstants() {
    throw new IllegalAccessError(ILLEGAL_ACCESS_EXCEPTION);
  }
}
