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

@ZenClass("mods.industrialupgrade.solidelectrolyzer")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTSolidStateElectrolyzer {

    @ZenMethod
    public static void addRecipe(IItemStack input, IItemStack outputStack, ILiquidStack output, ILiquidStack output1) {
        Recipes.recipes.addRecipe(
                "solid_electrolyzer",
                new BaseMachineRecipe(
                        new Input(
                                new InputItemStack(input)),
                        new RecipeOutput(null, CraftTweakerMC.getItemStacks(outputStack))
                )
        );

        Recipes.recipes.getRecipeFluid().addRecipe(
                "solid_electrolyzer",
                new BaseFluidMachineRecipe(
                        new InputFluid(
                                CraftTweakerMC.getItemStacks(input)[0]),
                        Arrays.asList(new FluidRecipeInput(output).getInputs(), new FluidRecipeInput(output1).getInputs())
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
            super("solid_electrolyzer");
            this.output = output;
            this.outputStack = outputStack;
        }


        public void apply() {
            Recipes.recipes.addFluidRemoveRecipe("solid_electrolyzer", new FluidRecipeInput(output).getInputs());
            Recipes.recipes.addRemoveRecipe("solid_electrolyzer", CraftTweakerMC.getItemStack(outputStack));

        }


    }

}
