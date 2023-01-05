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
import net.minecraft.nbt.NBTTagCompound;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.industrialupgrade.Fermer")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTFermer {

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient container) {
        Recipes.recipes.addRecipe(
                "farmer",
                new BaseMachineRecipe(
                        new Input(
                                new IC2RecipeInput(container)
                        ),
                        new RecipeOutput(null, CraftTweakerMC.getItemStacks(output))
                )
        );

    }

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient container, int time) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("operationLength", time);
        Recipes.recipes.addRecipe(
                "farmer",
                new BaseMachineRecipe(
                        new Input(
                                new IC2RecipeInput(container)
                        ),
                        new RecipeOutput(nbt, CraftTweakerMC.getItemStacks(output))
                )
        );

    }

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient container, int time, boolean consume) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("operationLength", time);
        nbt.setBoolean("consume", consume);
        Recipes.recipes.addRecipe(
                "farmer",
                new BaseMachineRecipe(
                        new Input(
                                new IC2RecipeInput(container)
                        ),
                        new RecipeOutput(nbt, CraftTweakerMC.getItemStacks(output))
                )
        );

    }

    @ZenMethod
    public static void remove(IItemStack output) {
        ModTweaker.LATE_REMOVALS.add(new CTFermer.Remove(output));
    }


    private static class Remove extends BaseAction {

        private final IItemStack output;

        public Remove(IItemStack output) {
            super("Fermer");
            this.output = output;
        }

        public void apply() {
            Recipes.recipes.removeRecipe("farmer", new RecipeOutput(null, CraftTweakerMC.getItemStacks(output)));
        }

        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(this.output);
        }

    }

}
