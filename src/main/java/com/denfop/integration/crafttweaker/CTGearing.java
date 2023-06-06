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
import crafttweaker.api.minecraft.CraftTweakerMC;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.industrialupgrade.gearing")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTGearing {

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient container) {
        Recipes.recipes.addAdderRecipe(
                "gearing",
                new BaseMachineRecipe(
                        new Input(
                                new IC2RecipeInput(container)
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
            super("gearing");
            this.output = output;
        }

        public void apply() {
            Recipes.recipes.addRemoveRecipe("gearing", CraftTweakerMC.getItemStack(output));

        }

        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(this.output);
        }

    }

}
