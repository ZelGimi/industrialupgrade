package com.denfop.integration.jei.oilpump;


import com.denfop.blocks.FluidName;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OilPumpHandler {

    private static final List<OilPumpHandler> recipes = new ArrayList<>();
    static Random rand = new Random();
    private final FluidStack input2;

    public OilPumpHandler(
            FluidStack input2
    ) {
        this.input2 = input2;
    }

    public static List<OilPumpHandler> getRecipes() { // Получатель всех рецептов.
        if (recipes.isEmpty()) {
            initRecipes();
        }
        return recipes;
    }

    public static OilPumpHandler addRecipe(
            FluidStack input2
    ) {
        OilPumpHandler recipe = new OilPumpHandler(input2);
        if (recipes.contains(recipe)) {
            return null;
        }
        recipes.add(recipe);
        return recipe;
    }

    public static OilPumpHandler getRecipe(ItemStack is) {
        if (is == null || is.isEmpty()) {
            return null;
        }
        for (OilPumpHandler recipe : recipes) {
            return recipe;
        }
        return null;
    }

    public static void initRecipes() {

        addRecipe(new FluidStack(FluidName.fluidneft.getInstance().get(), 1000 + rand.nextInt(9001)));


    }


    public FluidStack getOutput() { // Получатель входного предмета рецепта.
        return input2;
    }

}
