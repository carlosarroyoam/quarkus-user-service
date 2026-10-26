package com.carlosarroyoam.userservice.core.config;

import jakarta.enterprise.inject.Produces;
import java.time.Clock;

/** Configuración que produce los beans de infraestructura compartidos, p. ej. el {@link Clock}. */
public class ClockConfig {
  /**
   * Produce el reloj del sistema con la zona horaria por defecto.
   *
   * <p>Los servicios lo inyectan para obtener la fecha y la hora actuales en lugar de consultar
   * directamente el reloj del sistema, lo que permite sustituirlo en pruebas.
   *
   * @return el reloj del sistema
   */
  @Produces
  public Clock getClock() {
    return Clock.systemDefaultZone();
  }
}
