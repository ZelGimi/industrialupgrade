package com.denfop.integration.jei.recycler;


import com.denfop.IUItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecyclerHandler {

    private static final List<RecyclerHandler> recipes = new ArrayList<>();
    private final List<ItemStack> inputs;
    private final ItemStack output;

    public RecyclerHandler(ItemStack input, ItemStack output) {
        this(Collections.singletonList(input), output);
    }

    public RecyclerHandler(List<ItemStack> inputs, ItemStack output) {
        this.inputs = copyInputs(inputs);
        this.output = output;
    }

    private static List<ItemStack> copyInputs(List<ItemStack> inputs) {
        List<ItemStack> result = new ArrayList<>();
        if (inputs == null) {
            return result;
        }
        for (ItemStack input : inputs) {
            if (input == null || input.isEmpty() || input == ItemStack.EMPTY) {
                continue;
            }
            result.add(input.copy());
        }
        return result;
    }

    public static List<RecyclerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static RecyclerHandler addRecipe(ItemStack input, ItemStack output) {
        if (input.isEmpty() || input == ItemStack.EMPTY) {
            return null;
        }
        RecyclerHandler recipe = new RecyclerHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static RecyclerHandler addRecipe(List<ItemStack> inputs, ItemStack output) {
        RecyclerHandler recipe = new RecyclerHandler(inputs, output);
        if (recipe.inputs.isEmpty() || recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static RecyclerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (RecyclerHandler recipe : getRecipes()) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        recipes.clear();
        List<ItemStack> inputs = new ArrayList<>();
        for (Item container : BuiltInRegistries.ITEM) {
            ItemStack input = new ItemStack(container);
            if (!input.isEmpty() && input != ItemStack.EMPTY) {
                inputs.add(input);
            }
        }
        addRecipe(inputs, IUItem.scrap);
    }


    public ItemStack getInput() { // Получатель входного предмета рецепта.
        if (inputs.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return inputs.get(0).copy();
    }

    public List<ItemStack> getInputs() {
        return inputs;
    }

    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        for (ItemStack input : inputs) {
            if (is.getItem() == input.getItem()) {
                return true;
            }
        }
        return false;
    }

}
