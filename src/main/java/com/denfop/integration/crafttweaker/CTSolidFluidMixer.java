package com.denfop.integration.crafttweaker;


import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseFluidMachineRecipe;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.InputFluid;
import com.denfop.api.recipe.RecipeOutput;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;

@ZenClass("mods.industrialupgrade.solidfluidmixer")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTSolidFluidMixer {

    @ZenMethod
    public static void addRecipe(IItemStack input, ILiquidStack input_fluid, ILiquidStack output, ILiquidStack output1) {
        Recipes.recipes.addRecipe(
                "solid_fluid_mixer",
                new BaseMachineRecipe(
                        new Input(
                                new FluidRecipeInput(input_fluid).getInputs(),
                                new InputItemStack(input)
                        ),
                        new RecipeOutput(null, CraftTweakerMC.getItemStacks(input))
                )
        );

        Recipes.recipes.getRecipeFluid().addRecipe(
                "solid_fluid_mixer",
                new BaseFluidMachineRecipe(
                        new InputFluid(
                                new FluidRecipeInput(input_fluid).getInputs()),
                        Arrays.asList(
                                new FluidRecipeInput(output).getInputs(),
                                new FluidRecipeInput(output1).getInputs()
                        )
                )
        );
    }


    @ZenMethod
    public static void remove(ILiquidStack output, IItemStack input) {
        CraftTweakerAPI.apply(new Remove(output, input));
    }


    private static class Remove extends BaseAction {

        private final ILiquidStack output;
        private final IItemStack outputStack;

        public Remove(ILiquidStack output, final IItemStack outputStack) {
            super("solid_fluid_mixer");
            this.output = output;
            this.outputStack = outputStack;
        }


        public void apply() {
            Recipes.recipes.addFluidRemoveRecipe("solid_fluid_mixer", new FluidRecipeInput(output).getInputs());
            Recipes.recipes.addRemoveRecipe("solid_fluid_mixer", CraftTweakerMC.getItemStack(outputStack));

        }


    }

}
