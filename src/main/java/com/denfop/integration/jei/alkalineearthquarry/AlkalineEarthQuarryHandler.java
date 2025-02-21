package com.denfop.integration.jei.alkalineearthquarry;


import com.denfop.IUItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AlkalineEarthQuarryHandler {

    private static final List<AlkalineEarthQuarryHandler> recipes = new ArrayList<>();
    private final ItemStack input;
    private final double chance;
    private final ItemStack output;
    private final ItemStack mesh;

    public AlkalineEarthQuarryHandler(ItemStack input, double chance, ItemStack output, final ItemStack mesh) {
        this.input = input;
        this.chance = chance;
        this.output = output;
        this.mesh = mesh;
    }

    public static List<AlkalineEarthQuarryHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static AlkalineEarthQuarryHandler addRecipe(
            ItemStack input, double chance, ItemStack output, ItemStack mesh
    ) {
        AlkalineEarthQuarryHandler recipe = new AlkalineEarthQuarryHandler(input, chance, output, mesh);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static void initRecipes() {
        addRecipe(new ItemStack(Blocks.SAND), 0.5, new ItemStack(IUItem.ore2, 1, 0), new ItemStack(IUItem.ironMesh));
        addRecipe(new ItemStack(Blocks.SAND), 2.5, new ItemStack(IUItem.ore2, 1, 0), new ItemStack(IUItem.steelMesh));
        addRecipe(new ItemStack(Blocks.SAND), 4, new ItemStack(IUItem.ore2, 1, 0), new ItemStack(IUItem.boridehafniumMesh));
        addRecipe(new ItemStack(Blocks.SAND), 5, new ItemStack(IUItem.ore2, 1, 0), new ItemStack(IUItem.vanadiumaluminumMesh));
        addRecipe(new ItemStack(Blocks.SAND), 7, new ItemStack(IUItem.ore2, 1, 0), new ItemStack(IUItem.steleticMesh));


        addRecipe(new ItemStack(Blocks.DIRT), 0.5, new ItemStack(IUItem.ore2, 1, 1), new ItemStack(IUItem.steelMesh));
        addRecipe(new ItemStack(Blocks.DIRT), 2, new ItemStack(IUItem.ore2, 1, 1), new ItemStack(IUItem.boridehafniumMesh));
        addRecipe(new ItemStack(Blocks.DIRT), 3, new ItemStack(IUItem.ore2, 1, 1), new ItemStack(IUItem.vanadiumaluminumMesh));
        addRecipe(new ItemStack(Blocks.DIRT), 4, new ItemStack(IUItem.ore2, 1, 1), new ItemStack(IUItem.steleticMesh));


        addRecipe(new ItemStack(Blocks.GRAVEL), 0.5, new ItemStack(IUItem.ore2, 1, 2), new ItemStack(IUItem.steelMesh));
        addRecipe(new ItemStack(Blocks.GRAVEL), 0.5, new ItemStack(IUItem.ore2, 1, 2), new ItemStack(IUItem.boridehafniumMesh));
        addRecipe(new ItemStack(Blocks.GRAVEL), 1, new ItemStack(IUItem.ore2, 1, 2), new ItemStack(IUItem.vanadiumaluminumMesh));
        addRecipe(new ItemStack(Blocks.GRAVEL), 2, new ItemStack(IUItem.ore2, 1, 2), new ItemStack(IUItem.steleticMesh));


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

    public ItemStack getMesh() {
        return mesh;
    }


}
