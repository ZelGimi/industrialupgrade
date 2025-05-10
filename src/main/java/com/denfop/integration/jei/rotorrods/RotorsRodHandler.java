package com.denfop.integration.jei.rotorrods;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RotorsRodHandler {

    private static final List<RotorsRodHandler> recipes = new ArrayList<>();
    private final ItemStack output;
    private final ItemStack[] input;

    public RotorsRodHandler(ItemStack output, ItemStack... inputs) {
        this.input = inputs;
        this.output = output;
    }

    public static List<RotorsRodHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static RotorsRodHandler addRecipe(ItemStack output, ItemStack... inputs) {
        RotorsRodHandler recipe = new RotorsRodHandler(output, inputs);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }


    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("rod_assembler")) {
            addRecipe(
                    container.getOutput().items.get(0),
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.input.getInputs().get(2).getInputs().get(0),
                    container.input.getInputs().get(3).getInputs().get(0),
                    container.input.getInputs().get(4).getInputs().get(0),
                    container.input.getInputs().get(5).getInputs().get(0)
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
