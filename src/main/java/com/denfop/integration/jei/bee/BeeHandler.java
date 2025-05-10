package com.denfop.integration.jei.bee;


import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.mechanism.BlockHive;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BeeHandler {

    private static final List<BeeHandler> recipes = new ArrayList<>();
    private final ItemStack input;
    private final ItemStack output;


    public BeeHandler(ItemStack input, ItemStack output) {
        this.input = input;
        this.output = output;
    }

    public static List<BeeHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static BeeHandler addRecipe(
            ItemStack input, ItemStack output
    ) {
        BeeHandler recipe = new BeeHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static void initRecipes() {
        for (IMultiTileBlock container : BlockHive.values()) {
            addRecipe(
                    container.getDummyTe().getPickBlock(null, null),
                    container.getDummyTe().getSelfDrops(0, false).get(0)
            );


        }
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }


}
