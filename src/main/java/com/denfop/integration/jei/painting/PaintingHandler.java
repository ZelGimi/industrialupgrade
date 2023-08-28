package com.denfop.integration.jei.painting;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class PaintingHandler {

    private static final List<PaintingHandler> recipes = new ArrayList<>();
    public final NBTTagCompound metadata;
    private final ItemStack input, input1, output;

    public PaintingHandler(ItemStack input, ItemStack input1, ItemStack output, final NBTTagCompound metadata) {
        this.input = input;
        this.input1 = input1;
        this.output = output;
        this.metadata = metadata;
    }

    public static List<PaintingHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static PaintingHandler addRecipe(
            ItemStack input,
            ItemStack input1,
            ItemStack output,
            final NBTTagCompound metadata
    ) {
        PaintingHandler recipe = new PaintingHandler(input, input1, output, metadata);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static PaintingHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (PaintingHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("painter")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.getOutput().items.get(0), container.output.metadata
            );


        }
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

    public boolean matchesInput(ItemStack is) {
        return is.isItemEqual(input) || is.isItemEqual(input1);
    }

}
