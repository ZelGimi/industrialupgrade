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

@ZenClass("mods.industrialupgrade.fluid_mixer")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTFluidMixer {

    @ZenMethod
    public static void addRecipe(ILiquidStack input, ILiquidStack input1, ILiquidStack output, ILiquidStack output1) {


        Recipes.recipes.getRecipeFluid().addRecipe(
                "fluid_mixer",
                new BaseFluidMachineRecipe(new InputFluid(
                        new FluidRecipeInput(input).getInputs(),
                        new FluidRecipeInput(input1).getInputs()
                ), Arrays.asList(
                        new FluidRecipeInput(output).getInputs(),
                        (new FluidRecipeInput(output1).getInputs())
                ))
        );
    }


    @ZenMethod
    public static void remove(ILiquidStack output) {
        CraftTweakerAPI.apply(new Remove(output));
    }


    private static class Remove extends BaseAction {

        private final ILiquidStack output;

        public Remove(ILiquidStack output) {
            super("fluid_mixer");
            this.output = output;
        }


        public void apply() {
            Recipes.recipes.addFluidRemoveRecipe("fluid_mixer", new FluidRecipeInput(output).getInputs());

        }


    }

}
