package com.example;

import java.util.Set;

import com.example.domain.Fruit;

public interface FruitService {

	public Set<Fruit> getAll();

	public Fruit add(Fruit fruit);

}
