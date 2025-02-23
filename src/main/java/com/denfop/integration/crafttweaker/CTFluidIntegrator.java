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

import java.util.Collections;

@ZenClass("mods.industrialupgrade.fluidintegrator")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTFluidIntegrator {

    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack outputStack, ILiquidStack input_fluid, ILiquidStack output) {
        Recipes.recipes.addRecipe(
                "fluid_integrator",
                new BaseMachineRecipe(
                        new Input(
                                new FluidRecipeInput(input_fluid).getInputs(),
                                new InputItemStack(input)
                        ),
                        new RecipeOutput(null, CraftTweakerMC.getItemStacks(outputStack))
                )
        );

        Recipes.recipes.getRecipeFluid().addRecipe(
                "fluid_integrator",
                new BaseFluidMachineRecipe(
                        new InputFluid(
                                new FluidRecipeInput(input_fluid).getInputs()),
                        Collections.singletonList(new FluidRecipeInput(output).getInputs())
                )
        );
    }


    @ZenMethod
    public static void remove(ILiquidStack output, IItemStack outputStack) {
        CraftTweakerAPI.apply(new Remove(output, outputStack));
    }


    private static class Remove extends BaseAction {

        private final ILiquidStack output;
        private final IItemStack outputStack;

        public Remove(ILiquidStack output, final IItemStack outputStack) {
            super("fluid_integrator");
            this.output = output;
            this.outputStack = outputStack;
        }


        public void apply() {
            Recipes.recipes.addFluidRemoveRecipe("fluid_integrator", new FluidRecipeInput(output).getInputs());
            Recipes.recipes.addRemoveRecipe("fluid_integrator", CraftTweakerMC.getItemStack(outputStack));

        }


    }

}
