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

import java.util.Arrays;

@ZenClass("mods.industrialupgrade.oilrefiner")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTOilRefiner {

    @ZenMethod
    public static void addRecipe(ILiquidStack input, ILiquidStack output, ILiquidStack output1) {
        Recipes.recipes.getRecipeFluid().addRecipe("oil_refiner", new BaseFluidMachineRecipe(new InputFluid(
                new FluidRecipeInput(input).getInputs()), Arrays.asList(
                new FluidRecipeInput(output).getInputs(),
                (new FluidRecipeInput(output1).getInputs())
        )));
    }


    @ZenMethod
    public static void remove(ILiquidStack output) {
        CraftTweakerAPI.apply(new Remove(output));
    }


    private static class Remove extends BaseAction {

        private final ILiquidStack output;

        public Remove(ILiquidStack output) {
            super("oil_refiner");
            this.output = output;
        }


        public void apply() {
            Recipes.recipes.addFluidRemoveRecipe("oil_refiner", new FluidRecipeInput(output).getInputs());


        }


    }

}
