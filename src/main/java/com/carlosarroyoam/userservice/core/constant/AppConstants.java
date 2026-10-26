package com.carlosarroyoam.userservice.core.constant;

/** Contiene las constantes de uso general de la aplicación. No es instanciable. */
public class AppConstants {
  public static final String ILLEGAL_ACCESS_EXCEPTION = "Illegal access to utility class";

  /** Impide la instanciación de la clase de utilidad lanzando {@link IllegalAccessError}. */
  private AppConstants() {
    throw new IllegalAccessError(ILLEGAL_ACCESS_EXCEPTION);
  }
}
