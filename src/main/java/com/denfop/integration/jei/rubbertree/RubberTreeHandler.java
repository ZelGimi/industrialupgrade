package com.denfop.integration.jei.rubbertree;


import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.denfop.integration.jei.rubbertree.RubberTreeStructure.rubberTreesList;

public class RubberTreeHandler {

    private static final List<RubberTreeHandler> recipes = new ArrayList<>();


    Map<BlockPos, IBlockState> map;


    public RubberTreeHandler(Map<BlockPos, IBlockState> map) {
        this.map = map;
    }

    public static List<RubberTreeHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static RubberTreeHandler addRecipe(Map<BlockPos, IBlockState> map) {
        RubberTreeHandler recipe = new RubberTreeHandler(map);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static RubberTreeHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (RubberTreeHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {
        RubberTreeStructure.init();
        for (Map<BlockPos, IBlockState> container : rubberTreesList) {


            addRecipe(
                    container
            );


        }
    }

    public Map<BlockPos, IBlockState> getMap() {
        return map;
    }


}
