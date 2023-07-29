package com.carlosarroyoam.userservice.config;

import java.time.Clock;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class ClockConfig {

	@Produces
	public Clock getClock() {
		return Clock.systemUTC();
	}

}
