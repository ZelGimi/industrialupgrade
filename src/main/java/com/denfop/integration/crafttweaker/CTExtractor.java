package com.denfop.integration.crafttweaker;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.industrialupgrade.extractor")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTExtractor {

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient container) {
        Recipes.recipes.addRecipe(
                "extractor",
                new BaseMachineRecipe(
                        new Input(
                                new IC2RecipeInput(container)
                        ),
                        new RecipeOutput(null, CraftTweakerMC.getItemStacks(output))
                )
        );
        ic2.api.recipe.Recipes.extractor.addRecipe(new IC2RecipeInput(container), null, false,
                CraftTweakerMC.getItemStacks(output)
        );

    }


    @ZenMethod
    public static void remove(IItemStack output) {
        ModTweaker.LATE_REMOVALS.add(new Remove(output));
    }


    private static class Remove extends BaseAction {

        private final IItemStack output;

        public Remove(IItemStack output) {
            super("extractor");
            this.output = output;
        }

        public void apply() {
            Recipes.recipes.removeRecipe("extractor", new RecipeOutput(null, CraftTweakerMC.getItemStacks(output)));
        }

        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(this.output);
        }

    }

}
