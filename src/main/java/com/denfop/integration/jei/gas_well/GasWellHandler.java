package com.denfop.integration.jei.gas_well;


import com.denfop.Localization;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.blocks.FluidName;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class GasWellHandler {

    private static final List<GasWellHandler> recipes = new ArrayList<>();
    public final Fluid input;
    public final String output;


    public GasWellHandler(Fluid input, String output) {
        this.input = input;
        this.output = output;
    }

    public static List<GasWellHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GasWellHandler addRecipe(
            Fluid input, String output
    ) {
        GasWellHandler recipe = new GasWellHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static void initRecipes() {
        addRecipe(
                FluidName.fluidgas.getInstance(),
                Localization.translate("quarry.guide.gas_well7")
        );
        addRecipe(
                FluidName.fluidiodine.getInstance(),
                Localization.translate("quarry.guide.gas_well7")
        );
        addRecipe(
                FluidName.fluidfluor.getInstance(),
                Localization.translate("quarry.guide.gas_well7")
        );
        addRecipe(
                FluidName.fluidbromine.getInstance(),
                Localization.translate("quarry.guide.gas_well7")
        );
        addRecipe(
                FluidName.fluidchlorum.getInstance(),
                Localization.translate("quarry.guide.gas_well7")
        );
    }




}
