package com.example;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.jboss.logging.Logger;

import com.example.domain.Fruit;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FruitServiceImpl implements FruitService {

	private final Logger LOG = Logger.getLogger(FruitServiceImpl.class);
	private final Set<Fruit> fruits = new HashSet<>();

	@PostConstruct
	void init() {
		LOG.info("Creating fruits.");
		fruits.add(new Fruit().name("watermelon"));
		fruits.add(new Fruit().name("guava"));
	}

	@Override
	public Set<Fruit> getAll() {
		return fruits;
	}

	@Override
	public Fruit add(Fruit fruit) {
		if (findByName(fruit.getName()).isEmpty()) {
			fruits.add(fruit);
			LOG.info("Fruit added");
		}

		return fruit;
	}

	private Optional<Fruit> findByName(String name) {
		return fruits.stream().filter(fruit -> fruit.getName().equals(name)).findFirst();
	}

}
