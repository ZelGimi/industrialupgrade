package com.denfop.integration.crafttweaker;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.InputFluid;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.liquid.ILiquidStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Collections;

@ZenClass("mods.industrialupgrade.polymerizer")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTPolymerizer {

    @ZenMethod
    public static void addRecipe(ILiquidStack input, ILiquidStack output) {
        Recipes.recipes.getRecipeFluid().addRecipe("polymerizer", new BaseFluidMachineRecipe(new InputFluid(
                new FluidRecipeInput(input).getInputs()), Collections.singletonList(new FluidRecipeInput(output).getInputs())));
    }


    @ZenMethod
    public static void remove(ILiquidStack output) {
        CraftTweakerAPI.apply(new Remove(output));
    }


    private static class Remove extends BaseAction {

        private final ILiquidStack output;

        public Remove(ILiquidStack output) {
            super("refrigerator");
            this.output = output;
        }


        public void apply() {
            Recipes.recipes.addFluidRemoveRecipe("polymerizer", new FluidRecipeInput(output).getInputs());


        }


    }

}
