package com.denfop.integration.jei.alkalineearthquarry;


import com.denfop.IUItem;
import com.denfop.recipes.ItemStackHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class AlkalineEarthQuarryHandler {

    private static final List<AlkalineEarthQuarryHandler> recipes = new ArrayList<>();
    public final double chance;
    private final ItemStack input;
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
        addRecipe(new ItemStack(Blocks.SAND), 0.5, ItemStackHelper.fromData(IUItem.ore2, 1, 0), ItemStackHelper.fromData(IUItem.ironMesh));
        addRecipe(new ItemStack(Blocks.SAND), 2.5, ItemStackHelper.fromData(IUItem.ore2, 1, 0), ItemStackHelper.fromData(IUItem.steelMesh));
        addRecipe(new ItemStack(Blocks.SAND), 4, ItemStackHelper.fromData(IUItem.ore2, 1, 0), ItemStackHelper.fromData(IUItem.boridehafniumMesh));
        addRecipe(new ItemStack(Blocks.SAND), 5, ItemStackHelper.fromData(IUItem.ore2, 1, 0), ItemStackHelper.fromData(IUItem.vanadiumaluminumMesh));
        addRecipe(new ItemStack(Blocks.SAND), 7, ItemStackHelper.fromData(IUItem.ore2, 1, 0), ItemStackHelper.fromData(IUItem.steleticMesh));


        addRecipe(new ItemStack(Blocks.DIRT), 0.5, ItemStackHelper.fromData(IUItem.ore2, 1, 1), ItemStackHelper.fromData(IUItem.steelMesh));
        addRecipe(new ItemStack(Blocks.DIRT), 2, ItemStackHelper.fromData(IUItem.ore2, 1, 1), ItemStackHelper.fromData(IUItem.boridehafniumMesh));
        addRecipe(new ItemStack(Blocks.DIRT), 3, ItemStackHelper.fromData(IUItem.ore2, 1, 1), ItemStackHelper.fromData(IUItem.vanadiumaluminumMesh));
        addRecipe(new ItemStack(Blocks.DIRT), 4, ItemStackHelper.fromData(IUItem.ore2, 1, 1), ItemStackHelper.fromData(IUItem.steleticMesh));


        addRecipe(new ItemStack(Blocks.GRAVEL), 0.5, ItemStackHelper.fromData(IUItem.ore2, 1, 2), ItemStackHelper.fromData(IUItem.steelMesh));
        addRecipe(new ItemStack(Blocks.GRAVEL), 0.5, ItemStackHelper.fromData(IUItem.ore2, 1, 2), ItemStackHelper.fromData(IUItem.boridehafniumMesh));
        addRecipe(new ItemStack(Blocks.GRAVEL), 1, ItemStackHelper.fromData(IUItem.ore2, 1, 2), ItemStackHelper.fromData(IUItem.vanadiumaluminumMesh));
        addRecipe(new ItemStack(Blocks.GRAVEL), 2, ItemStackHelper.fromData(IUItem.ore2, 1, 2), ItemStackHelper.fromData(IUItem.steleticMesh));


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
