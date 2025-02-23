package com.denfop.api.crafting;

import com.denfop.recipe.IInputItemStack;

public class PartRecipe {

    private final String index;
    private final IInputItemStack input;

    public PartRecipe(String index, IInputItemStack input) {
        this.index = index;
        this.input = input;
    }

    public IInputItemStack getInput() {
        return input;
    }

    public String getIndex() {
        return index;
    }

}
