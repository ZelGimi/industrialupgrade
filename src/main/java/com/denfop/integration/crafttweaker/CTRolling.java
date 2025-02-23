package com.denfop.integration.crafttweaker;


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

@ZenClass("mods.industrialupgrade.rolling")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTRolling {

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient container) {
        Recipes.recipes.addAdderRecipe(
                "rolling",
                new BaseMachineRecipe(
                        new Input(
                                new InputItemStack(container)
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
            super("rolling");
            this.output = output;
        }

        public void apply() {
            Recipes.recipes.addRemoveRecipe("rolling", CraftTweakerMC.getItemStack(output));

        }


    }

}
