package com.denfop.integration.crafttweaker;

import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.utils.ModUtils;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.nbt.NBTTagCompound;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.industrialupgrade.centrifuge")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTCentrifuge {

    @ZenMethod
    public static void addRecipe(IIngredient container, short temperature, IItemStack... output) {
        final NBTTagCompound nbt = ModUtils.nbt();
        nbt.setShort("minHeat", temperature);

        Recipes.recipes.addRecipe(
                "centrifuge",
                new BaseMachineRecipe(
                        new Input(
                                new IC2RecipeInput(container)
                        ),
                        new RecipeOutput(nbt, CraftTweakerMC.getItemStacks(output))
                )
        );
        ic2.api.recipe.Recipes.centrifuge.addRecipe(new IC2RecipeInput(container), nbt, false,
                CraftTweakerMC.getItemStacks(output)
        );

    }


    @ZenMethod
    public static void remove(IItemStack output) {
        CraftTweakerAPI.apply(new Remove(output));
    }


    private static class Remove extends BaseAction {

        private final IItemStack input;

        public Remove(IItemStack input) {
            super("centrifuge");
            this.input = input;
        }

        public void apply() {
            Recipes.recipes.removeRecipe("centrifuge", CraftTweakerMC.getItemStacks(input)[0]);
        }

        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(this.input);
        }

    }

}
