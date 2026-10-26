package com.carlosarroyoam.userservice.auth;

import com.carlosarroyoam.userservice.user.entity.User;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Servicio que genera los tokens JWT de acceso de los {@link User} autenticados, firmados con el
 * emisor configurado en {@code app.jwt.sign.issuer}.
 */
@ApplicationScoped
public class TokenService {
  private final String issuer;

  /**
   * Crea el servicio con el emisor de los tokens.
   *
   * @param issuer emisor incluido en la reclamación {@code iss} de cada token
   */
  public TokenService(@ConfigProperty(name = "app.jwt.sign.issuer") String issuer) {
    this.issuer = issuer;
  }

  /**
   * Genera un token JWT firmado para un usuario. El nombre de usuario se emite como {@code upn} y
   * el título de su rol como único grupo.
   *
   * @param user usuario autenticado
   * @return el token JWT firmado
   */
  public String generateToken(User user) {
    HashSet<String> roles =
        Stream.of(user.getRole().getTitle()).collect(Collectors.toCollection(HashSet::new));

    return Jwt.issuer(issuer).upn(user.getUsername()).groups(roles).sign();
  }
}
