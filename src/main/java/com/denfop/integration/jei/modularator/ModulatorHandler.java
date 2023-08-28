package com.denfop.integration.jei.modularator;


import com.denfop.IUCore;
import com.denfop.api.recipe.RecipeInputStack;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ModulatorHandler {

    private static final List<ModulatorHandler> recipes = new ArrayList<>();
    private final ItemStack output;

    public ModulatorHandler(ItemStack output) {
        this.output = output;
    }

    public static List<ModulatorHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static ModulatorHandler addRecipe(ItemStack output) {
        ModulatorHandler recipe = new ModulatorHandler(output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static ModulatorHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (ModulatorHandler recipe : recipes) {
            if (recipe.matchesInput(is)) {
                return recipe;
            }
        }
        return null;
    }

    public static void initRecipes() {
        for (RecipeInputStack container : IUCore.get_all_list) {
            addRecipe(container.getItemStack().get(0));

        }
    }


    public ItemStack getOutput() { // Получатель выходного предмета рецепта.
        return output.copy();
    }

    public boolean matchesInput(ItemStack is) {
        return is.getItem() == output.getItem();
    }

}
