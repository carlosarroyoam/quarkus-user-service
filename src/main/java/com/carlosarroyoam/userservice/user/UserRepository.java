package com.carlosarroyoam.userservice.user;

import com.carlosarroyoam.userservice.user.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

/** Repositorio Panache de {@link User} con consultas por nombre de usuario y correo. */
@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
  /**
   * Busca un usuario por su nombre de usuario.
   *
   * @param username nombre de usuario
   * @return el usuario encontrado, si existe
   */
  public Optional<User> findByUsernameOptional(String username) {
    return find("username", username).firstResultOptional();
  }

  /**
   * Busca un usuario por su correo electrónico.
   *
   * @param email correo electrónico
   * @return el usuario encontrado, si existe
   */
  public Optional<User> findByEmailOptional(String email) {
    return find("email", email).firstResultOptional();
  }
}
