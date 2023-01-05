package com.denfop.integration.jei.genobs;


import com.denfop.api.IObsidianGenerator;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.RecipeOutput;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GenObsHandler {

    private static final List<GenObsHandler> recipes = new ArrayList<>();
    private final FluidStack input2;
    private final FluidStack input;
    private final ItemStack output;


    public GenObsHandler(
            FluidStack input, FluidStack input2, ItemStack output
    ) {
        this.input = input;
        this.input2 = input2;
        this.output = output;
    }

    public static List<GenObsHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GenObsHandler addRecipe(
            FluidStack input, FluidStack input2, ItemStack output
    ) {
        GenObsHandler recipe = new GenObsHandler(input, input2, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GenObsHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GenObsHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {
        for (Map.Entry<IObsidianGenerator.Input, RecipeOutput> container :
                Recipes.obsidianGenerator.getRecipes().entrySet()) {
            addRecipe(container.getKey().fluidStack, container.getKey().fluidStack1, container.getValue().items.get(0));

        }
    }


    public FluidStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }

    public FluidStack getInput1() { // Получатель входного предмета рецепта.
        return input2;
    }

    public ItemStack getOutput() { // Получатель входного предмета рецепта.
        return output;
    }

}
