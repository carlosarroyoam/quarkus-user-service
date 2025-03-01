package com.carlosarroyoam.userservice.config;

import jakarta.enterprise.inject.Produces;
import java.time.Clock;

public class ClockConfig {
  @Produces
  public Clock getClock() {
    return Clock.systemDefaultZone();
  }
}
