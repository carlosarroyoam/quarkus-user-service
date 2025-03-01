package com.carlosarroyoam.userservice.service;

import com.carlosarroyoam.userservice.constant.AppConstants;
import com.carlosarroyoam.userservice.entity.User;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class TokenService {
  private final String issuer;

  public TokenService(@ConfigProperty(name = AppConstants.JWT_ISSUER_PROPERTY) String issuer) {
    this.issuer = issuer;
  }

  public String generateToken(User user) {
    HashSet<String> roles = new HashSet<>(
        Stream.of(user.getRole().getTitle()).collect(Collectors.toSet()));

    return Jwt.issuer(issuer).upn(user.getUsername()).groups(roles).sign();
  }
}
