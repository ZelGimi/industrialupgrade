package com.denfop.integration.jei.crops;


import com.denfop.api.agriculture.CropInit;
import com.denfop.api.agriculture.CropNetwork;
import com.denfop.api.agriculture.ICrop;
import com.denfop.blocks.FluidName;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class CropHandler {

    private static final List<CropHandler> recipes = new ArrayList<>();
    public final ICrop output;


    public CropHandler(
            ICrop output
    ) {
        this.output = output;
    }

    public static List<CropHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static CropHandler addRecipe(
            ICrop output
    ) {
        CropHandler recipe = new CropHandler(output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static CropHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        return recipes.get(0);
    }

    public static void initRecipes() {

        CropNetwork.instance.getCropMap().forEach((integer, crop) -> {
            if (crop.getId() != 3)
                addRecipe(crop);
        });


    }




}
