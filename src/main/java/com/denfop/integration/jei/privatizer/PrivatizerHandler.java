package com.denfop.integration.jei.privatizer;


import com.denfop.IUItem;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PrivatizerHandler {

    private static final List<PrivatizerHandler> recipes = new ArrayList<>();
    private final ItemStack output;

    public PrivatizerHandler(ItemStack output) {
        this.output = output;
    }

    public static List<PrivatizerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static PrivatizerHandler addRecipe(ItemStack output) {
        PrivatizerHandler recipe = new PrivatizerHandler(output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static PrivatizerHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (PrivatizerHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {

        addRecipe(new ItemStack(IUItem.entitymodules.getStack(0), 1));


    }


    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return is.getItem() == output.getItem();
    }

}
