package com.carlosarroyoam.userservice.repositories;

import com.carlosarroyoam.userservice.model.User;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

	public User findByUsername(String username) {
		return find("username", username).firstResult();
	}

}
