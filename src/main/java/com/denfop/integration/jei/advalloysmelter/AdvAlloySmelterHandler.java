package com.denfop.integration.jei.advalloysmelter;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AdvAlloySmelterHandler {

    public static final List<AdvAlloySmelterHandler> recipes = new ArrayList<>();
    public final ItemStack input, input1, input2, output;
    public final short temperature;

    public AdvAlloySmelterHandler(
            ItemStack input,
            ItemStack input1,
            ItemStack input2,
            ItemStack output,
            final short temperature
    ) {
        this.input = input;
        this.input1 = input1;
        this.input2 = input2;
        this.output = output;
        this.temperature = temperature;
    }

    public static List<AdvAlloySmelterHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static AdvAlloySmelterHandler addRecipe(
            ItemStack input,
            ItemStack input1,
            ItemStack input2,
            ItemStack output,
            final short temperature
    ) {
        AdvAlloySmelterHandler recipe = new AdvAlloySmelterHandler(input, input1, input2, output, temperature);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static AdvAlloySmelterHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (AdvAlloySmelterHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("advalloysmelter")) {
            addRecipe(container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0), container.input.getInputs().get(2).getInputs().get(0),
                    container.getOutput().items.get(0), container.getOutput().metadata.getShort("temperature")
            );


        }
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

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return is.isItemEqual(input) || is.isItemEqual(input1) || is.isItemEqual(input2);
    }

}
