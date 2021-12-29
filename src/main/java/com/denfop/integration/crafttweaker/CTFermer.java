package com.denfop.integration.crafttweaker;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import com.denfop.api.Recipes;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import ic2.api.recipe.RecipeOutput;
import net.minecraft.nbt.NBTTagCompound;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Collections;

@ZenClass("mods.industrialupgrade.Fermer")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTFermer {
    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient container) {
        CraftTweakerAPI.apply(new MachineAddRecipeAction("Fermer", Recipes.fermer,

                CraftTweakerMC.getItemStacks(output), null, new IC2RecipeInput(container)));
    }

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient container, int time) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("operationLength", time);
        CraftTweakerAPI.apply(new MachineAddRecipeAction("Fermer", Recipes.fermer,

                CraftTweakerMC.getItemStacks(output), nbt, new IC2RecipeInput(container)));
    }

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient container, int time, boolean consume) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("operationLength", time);
        nbt.setBoolean("consume", consume);
        CraftTweakerAPI.apply(new MachineAddRecipeAction("Fermer", Recipes.fermer,

                CraftTweakerMC.getItemStacks(output), nbt, new IC2RecipeInput(container)));
    }

    @ZenMethod
    public static void remove(IItemStack input) {
        ModTweaker.LATE_REMOVALS.add(new CTFermer.Remove(input));
    }

    @ZenMethod
    public static IItemStack[] getOutput(IItemStack input) {
        RecipeOutput output = Recipes.molecular.getOutputFor(CraftTweakerMC.getItemStack(input), false);
        if (output == null || output.items.isEmpty())
            return null;
        return CraftTweakerMC.getIItemStacks(output.items);
    }

    private static class Remove extends BaseAction {
        private IItemStack input;

        public Remove(IItemStack input) {
            super("Fermer");
            this.input = input;
        }

        public void apply() {
            RecipeOutput output = Recipes.fermer.getOutputFor(CraftTweakerMC.getItemStack(input), false);
            if (output == null || output.items.isEmpty())
                return;
            Recipes.fermer.removeRecipe(CraftTweakerMC.getItemStack(input), Collections.singletonList(output.items.get(0)));

        }

        protected String getRecipeInfo() {
            return LogHelper.getStackDescription((IIngredient)this.input);
        }
    }
}
