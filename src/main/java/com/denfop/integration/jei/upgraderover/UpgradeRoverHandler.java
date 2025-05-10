package com.denfop.integration.jei.upgraderover;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class UpgradeRoverHandler {

    private static final List<UpgradeRoverHandler> recipes = new ArrayList<>();
    public final CompoundTag metadata;
    private final ItemStack input, input1, output;

    public UpgradeRoverHandler(
            ItemStack input,
            ItemStack input1,
            ItemStack output,
            final CompoundTag metadata
    ) {
        this.input = input;
        this.input1 = input1;
        this.output = output;
        this.metadata = metadata;
    }

    public static List<UpgradeRoverHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static UpgradeRoverHandler addRecipe(
            ItemStack input,
            ItemStack input1,
            ItemStack output,
            final CompoundTag metadata
    ) {
        UpgradeRoverHandler recipe = new UpgradeRoverHandler(input, input1, output, metadata);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static UpgradeRoverHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (UpgradeRoverHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("roverupgradeblock")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.input.getInputs().get(1).getInputs().get(0),
                    container.getOutput().items.get(0), container.output.metadata
            );


        }
    }


    public ItemStack getInput() {
        return input;
    }

    public ItemStack getInput1() {
        return input1;
    }

    public ItemStack getOutput() {
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return true;
    }

}
