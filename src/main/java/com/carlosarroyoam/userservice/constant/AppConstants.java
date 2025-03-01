package com.carlosarroyoam.userservice.constant;

public class AppConstants {
  public static final String ILLEGAL_ACCESS_EXCEPTION_MESSAGE = "Illegal access to class";
  public static final String JWT_ISSUER_PROPERTY = "com.carlosarroyoam.jwt.sing.issuer";

  private AppConstants() {
    throw new IllegalAccessError(ILLEGAL_ACCESS_EXCEPTION_MESSAGE);
  }
}
