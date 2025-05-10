package com.denfop.integration.jei.stamp;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StampHandler {

    private static final List<StampHandler> recipes = new ArrayList<>();
    private final ItemStack input, input1, input2, input3, output;
    private final String name;

    public StampHandler(
            ItemStack input, ItemStack input1, ItemStack input2, ItemStack input3,

            ItemStack output, String name
    ) {
        this.input = input;
        this.input1 = input1;
        this.input2 = input2;
        this.input3 = input3;
        this.output = output;
        this.name = name;
    }

    public static List<StampHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static StampHandler addRecipe(
            ItemStack input, ItemStack input1, ItemStack input2, ItemStack input3,
            ItemStack output, String name
    ) {
        StampHandler recipe = new StampHandler(input, input1, input2, input3, output, name);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static StampHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (StampHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("stamp_vent")) {
            addRecipe(container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.input.getInputs().get(2).getInputs().get(0),
                    container.input.getInputs().get(3).getInputs().get(0),
                    container.getOutput().items.get(0), "stamp_vent"

            );
        }
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("stamp_plate")) {
            addRecipe(container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.input.getInputs().get(2).getInputs().get(0),
                    container.input.getInputs().get(3).getInputs().get(0),
                    container.getOutput().items.get(0), "stamp_plate"

            );
        }
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("stamp_exchanger")) {
            addRecipe(container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.input.getInputs().get(2).getInputs().get(0),
                    container.input.getInputs().get(3).getInputs().get(0),
                    container.getOutput().items.get(0), "stamp_exchanger"

            );
        }
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("stamp_coolant")) {
            addRecipe(container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.input.getInputs().get(2).getInputs().get(0),
                    container.input.getInputs().get(3).getInputs().get(0),
                    container.getOutput().items.get(0), "stamp_coolant"

            );
        }
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("stamp_capacitor")) {
            addRecipe(container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.input.getInputs().get(2).getInputs().get(0),
                    container.input.getInputs().get(3).getInputs().get(0),
                    container.getOutput().items.get(0), "stamp_capacitor"

            );
        }

    }

    public String getName() {
        return name;
    }

    public ItemStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }

    public ItemStack getInput1() { // Получатель входного предмета рецепта.
        return input1;
    }

    public ItemStack getInput2() { // Получатель входного предмета рецепта.
        return input2;
    }

    public ItemStack getInput3() { // Получатель входного предмета рецепта.
        return input3;
    }


    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return true;
    }
    public List<ItemStack> getInputs() {
        return Arrays.asList(input, input1, input2, input3);
    }

}
