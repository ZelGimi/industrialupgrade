package com.denfop.integration.jei.analyzer;


import com.denfop.IUItem;
import com.denfop.blocks.BlockHeavyOre;
import com.denfop.blocks.BlockMineral;
import com.denfop.recipes.ItemStackHelper;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AnalyzerHandler {

    private static final List<AnalyzerHandler> recipes = new ArrayList<>();
    private final ItemStack input;


    public AnalyzerHandler(ItemStack input) {
        this.input = input;
    }

    public static List<AnalyzerHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static AnalyzerHandler addRecipe(
            ItemStack input
    ) {
        AnalyzerHandler recipe = new AnalyzerHandler(input);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static void initRecipes() {
        for (BlockHeavyOre.Type type : BlockHeavyOre.Type.values()) {
            addRecipe(ItemStackHelper.fromData(IUItem.heavyore, 1, type.getMetadata())
            );


        }
        for (BlockMineral.Type type : BlockMineral.Type.values()) {
            addRecipe(ItemStackHelper.fromData(IUItem.mineral, 1, type.getMetadata())
            );


        }
    }

    public ItemStack getInput() {
        return input;
    }


}
