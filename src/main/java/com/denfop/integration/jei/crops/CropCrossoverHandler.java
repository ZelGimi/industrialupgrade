package com.denfop.integration.jei.crops;


import com.denfop.api.agriculture.CropNetwork;
import com.denfop.api.agriculture.ICrop;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CropCrossoverHandler {

    private static final List<CropCrossoverHandler> recipes = new ArrayList<>();
    public final ICrop output;
    public final List<ICrop> inputs;


    public CropCrossoverHandler(
            ICrop output, List<ICrop> inputs
    ) {
        this.output = output;
        this.inputs = inputs;
    }

    public static List<CropCrossoverHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static CropCrossoverHandler addRecipe(
            ICrop output, List<ICrop> inputs
    ) {
        CropCrossoverHandler recipe = new CropCrossoverHandler(output, inputs);

        recipes.add(recipe);
        return recipe;
    }

    public static CropCrossoverHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }

    public static void initRecipes() {

        CropNetwork.instance.getCropMap().forEach((integer, crop) -> {
            if (crop.isCombine() && !crop.getCropCombine().isEmpty()) {
                addRecipe(crop, crop.getCropCombine());
            }
        });


    }


}
