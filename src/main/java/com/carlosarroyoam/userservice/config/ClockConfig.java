package com.carlosarroyoam.userservice.config;

import java.time.Clock;

import jakarta.enterprise.inject.Produces;

public class ClockConfig {

	@Produces
	public Clock getClock() {
		return Clock.systemDefaultZone();
	}

}
