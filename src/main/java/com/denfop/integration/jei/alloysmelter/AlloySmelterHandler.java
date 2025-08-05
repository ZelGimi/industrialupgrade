package com.denfop.integration.jei.alloysmelter;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AlloySmelterHandler {

    private static final List<AlloySmelterHandler> recipes = new ArrayList<>();
    public final ItemStack input, input1, output;
    public final short temperature;
    private final BaseMachineRecipe container;

    public AlloySmelterHandler(ItemStack input, ItemStack input1, ItemStack output, final short temperature, BaseMachineRecipe container) {
        this.input = input;
        this.input1 = input1;
        this.output = output;
        this.temperature = temperature;
        this.container = container;
    }

    public static List<AlloySmelterHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static AlloySmelterHandler addRecipe(ItemStack input, ItemStack input1, ItemStack output, final short temperature, BaseMachineRecipe container) {
        AlloySmelterHandler recipe = new AlloySmelterHandler(input, input1, output, temperature, container);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static AlloySmelterHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (AlloySmelterHandler recipe : recipes) {

            return recipe;

        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("alloysmelter")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.getOutput().items.get(0),
                    container.getOutput().metadata.getShort("temperature"), container
            );


        }
    }

    public BaseMachineRecipe getContainer() {
        return container;
    }

    public ItemStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }

    public ItemStack getInput1() { // Получатель входного предмета рецепта.
        return input1;
    }

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }


}
