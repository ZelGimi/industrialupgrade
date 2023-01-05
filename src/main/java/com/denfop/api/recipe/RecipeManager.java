package com.denfop.api.recipe;

public class RecipeManager implements IBaseRecipe {

    private final String name;
    private final int size;
    private final boolean consume;
    private final boolean require;

    public RecipeManager(String name, int size, boolean consume) {
        this.name = name;
        this.size = size;
        this.consume = consume;
        this.require = false;
    }

    public RecipeManager(String name, int size, boolean consume, boolean require) {
        this.name = name;
        this.size = size;
        this.consume = consume;
        this.require = require;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean consume() {
        return this.consume;
    }

    @Override
    public boolean require() {
        return this.require;
    }

}
