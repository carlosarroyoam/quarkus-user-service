package com.example.services;

import java.util.Set;

import com.example.model.Fruit;

public interface FruitService {

	public Set<Fruit> getAll();

	public Fruit add(Fruit fruit);

}
