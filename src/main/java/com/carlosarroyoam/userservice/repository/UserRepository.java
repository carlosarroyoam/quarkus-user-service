package com.carlosarroyoam.userservice.repository;

import com.carlosarroyoam.userservice.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
  public Optional<User> findByUsernameOptional(String username) {
    return find("username", username).firstResultOptional();
  }

  public Optional<User> findByEmailOptional(String email) {
    return find("email", email).firstResultOptional();
  }
}
