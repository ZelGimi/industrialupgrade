package com.denfop.integration.jei.gassensor;


import com.denfop.Localization;
import com.denfop.blocks.FluidName;
import net.minecraftforge.fluids.Fluid;

import java.util.ArrayList;
import java.util.List;

public class GasSensorHandler {

    private static final List<GasSensorHandler> recipes = new ArrayList<>();
    public final Fluid input;
    public final String output;


    public GasSensorHandler(Fluid input, String output) {
        this.input = input;
        this.output = output;
    }

    public static List<GasSensorHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static GasSensorHandler addRecipe(
            Fluid input, String output
    ) {
        GasSensorHandler recipe = new GasSensorHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static void initRecipes() {
        addRecipe(
                FluidName.fluidgas.getInstance(),
                Localization.translate("iu.gas_find.info")
        );
        addRecipe(
                FluidName.fluidiodine.getInstance(),
                Localization.translate("iu.iodine_find.info")
        );
        addRecipe(
                FluidName.fluidfluor.getInstance(),
                Localization.translate("iu.iodine_find.info")
        );
        addRecipe(
                FluidName.fluidbromine.getInstance(),
                Localization.translate("iu.bromine_find.info")
        );
        addRecipe(
                FluidName.fluidchlorum.getInstance(),
                Localization.translate("iu.chlorine_find.info")
        );
    }


}
