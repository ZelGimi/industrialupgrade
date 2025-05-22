package com.denfop.integration.jei.apiary;


import com.denfop.Localization;
import com.denfop.blocks.FluidName;
import net.minecraftforge.fluids.Fluid;

import java.util.ArrayList;
import java.util.List;

public class ApiaryHandler {

    private static final List<ApiaryHandler> recipes = new ArrayList<>();
    public final Fluid input;
    public final String output;


    public ApiaryHandler(Fluid input, String output) {
        this.input = input;
        this.output = output;
    }

    public static List<ApiaryHandler> getRecipes() {
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static ApiaryHandler addRecipe(
            Fluid input, String output
    ) {
        ApiaryHandler recipe = new ApiaryHandler(input, output);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static void initRecipes() {
        addRecipe(
                FluidName.fluidhoney.getInstance(),
                Localization.translate("iu.apiary.honey")
        );
        addRecipe(
                FluidName.fluidroyaljelly.getInstance(),
                Localization.translate("iu.apiary.royaljelly")
        );
    }


}
