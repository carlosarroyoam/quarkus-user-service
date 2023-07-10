package com.carlosarroyoam.userservice.services;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.carlosarroyoam.userservice.constants.AppConstants;
import com.carlosarroyoam.userservice.model.User;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TokenService {

	@ConfigProperty(name = AppConstants.JWT_ISSUER_PROPERTY)
	private String issuer;

	public String generateToken(User user) {
		return Jwt.issuer(issuer).upn(user.getUsername())
				.groups(new HashSet<>(Stream.of(user.getRole().split(",")).collect(Collectors.toSet()))).sign();
	}

}
