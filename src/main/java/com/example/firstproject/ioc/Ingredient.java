package com.example.firstproject.ioc;

public abstract class Ingredient {
    public String name;

    public Ingredient(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
