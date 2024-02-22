package com.carlosarroyoam.userservice.repository;

import com.carlosarroyoam.userservice.model.Role;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RoleRepository implements PanacheRepository<Role> {
}
