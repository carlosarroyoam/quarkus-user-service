package com.carlosarroyoam.userservice.core.constant;

public class AppConstants {
  public static final String ILLEGAL_ACCESS_EXCEPTION = "Illegal access to utility class";

  private AppConstants() {
    throw new IllegalAccessError(ILLEGAL_ACCESS_EXCEPTION);
  }
}
