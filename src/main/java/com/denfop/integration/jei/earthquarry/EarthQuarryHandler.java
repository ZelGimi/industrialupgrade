package com.denfop.integration.jei.earthquarry;


import com.denfop.IUItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EarthQuarryHandler {

    private static final List<EarthQuarryHandler> recipes = new ArrayList<>();
    private final ItemStack input;
    private final double chance;
    private final ItemStack output;

    public EarthQuarryHandler(ItemStack input, double chance, ItemStack output) {
        this.input = input;
        this.chance = chance;
        this.output = output;
    }

    public static List<EarthQuarryHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static EarthQuarryHandler addRecipe(
            ItemStack input, double chance, ItemStack output
    ) {
        EarthQuarryHandler recipe = new EarthQuarryHandler(input, chance, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static void initRecipes() {
        addRecipe(new ItemStack(Blocks.DIRT), 10, new ItemStack(IUItem.ore2, 1, 1));
        addRecipe(new ItemStack(Blocks.GRAVEL), 6, new ItemStack(IUItem.ore2, 1, 2));
        addRecipe(new ItemStack(Blocks.SAND), 20, new ItemStack(IUItem.ore2, 1, 0));
    }

    public double getChance() {
        return chance;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }


}
