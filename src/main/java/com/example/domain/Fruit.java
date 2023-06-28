package com.example.domain;

public class Fruit {

	private String name;

	public Fruit name(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Fruit)) {
			return false;
		}

		Fruit fruit = (Fruit) obj;

		return this.name == fruit.getName();
	}

}
