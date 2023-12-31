package com.denfop.integration.crafttweaker;

import com.blamejared.mtlib.helpers.LogHelper;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.liquid.ILiquidStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.industrialupgrade.ChemicalFactory")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTChemicalFactory {

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient container, IIngredient container1, ILiquidStack liquidStack) {
        Recipes.recipes.addAdderRecipe(
                "plastic",
                new BaseMachineRecipe(
                        new Input(new IC2FluidRecipeInput(liquidStack).getInputs(),
                                new IC2InputItemStack(container), new IC2InputItemStack(container1)
                        ),
                        new RecipeOutput(null, CraftTweakerMC.getItemStacks(output))
                )
        );


    }


    @ZenMethod
    public static void remove(IItemStack output) {
        CraftTweakerAPI.apply(new Remove(output));
    }


    private static class Remove extends BaseAction {

        private final IItemStack output;

        public Remove(IItemStack output) {
            super("plastic");
            this.output = output;
        }

        public void apply() {
            Recipes.recipes.addRemoveRecipe("plastic", CraftTweakerMC.getItemStack(output));


        }

        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(this.output);
        }

    }

}
