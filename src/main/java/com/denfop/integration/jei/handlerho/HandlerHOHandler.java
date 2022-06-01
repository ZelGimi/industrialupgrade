package com.denfop.integration.jei.handlerho;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class HandlerHOHandler {

    protected static final List<HandlerHOHandler> recipes = new ArrayList<>();
    protected final List<ItemStack> output;
    protected final NBTTagCompound nbt;
    protected final ItemStack input;

    public HandlerHOHandler(ItemStack input, List<ItemStack> output, final NBTTagCompound metaData) {
        this.input = input;
        this.output = output;
        this.nbt = metaData;
    }

    public static List<HandlerHOHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static HandlerHOHandler addRecipe(
            ItemStack input,
            List<ItemStack> output,
            final NBTTagCompound metaData
    ) {
        HandlerHOHandler recipe = new HandlerHOHandler(input, output, metaData);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static HandlerHOHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (HandlerHOHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (BaseMachineRecipe container : Recipes.recipes.getRecipeList("handlerho")) {
            addRecipe(
                    container.input.getInputs().get(0).getInputs().get(0),
                    container.getOutput().items, container.getOutput().metadata
            );


        }
    }


    public ItemStack getInput() { // Получатель входного предмета рецепта.
        return input;
    }

    public List<ItemStack> getOutput() { // Получатель выходного предмета рецепта.
        return output;
    }

    public boolean matchesInput(ItemStack is) {
        return is.getItem() == input.getItem();
    }

    public NBTTagCompound getNBT() {
        return this.nbt;
    }

}
