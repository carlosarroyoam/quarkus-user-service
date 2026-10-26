package com.carlosarroyoam.userservice.auth;

import com.carlosarroyoam.userservice.user.entity.User;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class TokenService {
  private final String issuer;

  public TokenService(@ConfigProperty(name = "app.jwt.sign.issuer") String issuer) {
    this.issuer = issuer;
  }

  public String generateToken(User user) {
    HashSet<String> roles =
        Stream.of(user.getRole().getTitle()).collect(Collectors.toCollection(HashSet::new));

    return Jwt.issuer(issuer).upn(user.getUsername()).groups(roles).sign();
  }
}
