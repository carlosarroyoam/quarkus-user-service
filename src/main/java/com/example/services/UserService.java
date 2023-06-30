package com.example.services;

import java.util.List;

import com.example.model.User;
import com.example.repositories.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class UserService {

	private final UserRepository userRepository;

	@Inject
	public UserService(final UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	public List<User> findAll() {
		return userRepository.listAll();
	}

	public User findById(Long id) {
		User userById = userRepository.findById(id);

		if (userById == null) {
			throw new NotFoundException();
		}

		return userById;
	}

	@Transactional
	public User create(User user) {
		User userByUsername = userRepository.findByUsername(user.getUsername());

		if (userByUsername != null) {
			throw new BadRequestException("User already exists");
		}

		userRepository.persist(user);

		return user;
	}

}
