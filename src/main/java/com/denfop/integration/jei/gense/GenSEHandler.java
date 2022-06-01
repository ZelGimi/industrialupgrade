package com.denfop.integration.jei.gense;


import com.denfop.IUItem;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GenSEHandler {

    private static final List<GenSEHandler> recipes = new ArrayList<>();
    private final ItemStack output;

    public GenSEHandler(ItemStack output) {
        this.output = output;
    }

    public static List<GenSEHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GenSEHandler addRecipe(ItemStack output) {
        GenSEHandler recipe = new GenSEHandler(output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static GenSEHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (GenSEHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {
        addRecipe(new ItemStack(IUItem.sunnarium, 1, 4));


    }


    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

}
