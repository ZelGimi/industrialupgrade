package com.denfop.integration.jei.upgradeblock;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class UpgradeBlockHandler {

    private static final List<UpgradeBlockHandler> recipes = new ArrayList<>();
    public final NBTTagCompound metadata;
    private final ItemStack input, input1, output;

    public UpgradeBlockHandler(
            ItemStack input,
            ItemStack input1,
            ItemStack output,
            final NBTTagCompound metadata
    ) {
        this.input = input;
        this.input1 = input1;
        this.output = output;
        this.metadata = metadata;
    }

    public static List<UpgradeBlockHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static UpgradeBlockHandler addRecipe(
            ItemStack input,
            ItemStack input1,
            ItemStack output,
            final NBTTagCompound metadata
    ) {
        UpgradeBlockHandler recipe = new UpgradeBlockHandler(input, input1, output, metadata);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static UpgradeBlockHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (UpgradeBlockHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("upgradeblock")) {
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
