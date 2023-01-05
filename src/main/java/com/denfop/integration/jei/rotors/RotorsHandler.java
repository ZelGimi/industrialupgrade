package com.denfop.integration.jei.rotors;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RotorsHandler {

    private static final List<RotorsHandler> recipes = new ArrayList<>();
    private final ItemStack output;
    private final ItemStack[] input;

    public RotorsHandler(ItemStack output, ItemStack... inputs) {
        this.input = inputs;
        this.output = output;
    }

    public static List<RotorsHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static RotorsHandler addRecipe(ItemStack output, ItemStack... inputs) {
        RotorsHandler recipe = new RotorsHandler(output, inputs);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("rotor_assembler")) {
            addRecipe(
                    container.getOutput().items.get(0),
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.input.getInputs().get(2).getInputs().get(0),
                    container.input.getInputs().get(3).getInputs().get(0),
                    container.input.getInputs().get(4).getInputs().get(0)
            );


        }
    }


    public ItemStack[] getInputs() { // Получатель входного предмета рецепта.
        return input;
    }

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }


}
