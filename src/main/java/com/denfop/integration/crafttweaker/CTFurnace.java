package com.denfop.integration.crafttweaker;


import com.denfop.IUCore;
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
import net.minecraft.nbt.NBTTagCompound;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;

@ZenClass("mods.industrialupgrade.electricfurnace")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTFurnace {

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient container, float experience) {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.setFloat("experience", experience);

        Recipes.recipes.addAdderRecipe(
                "furnace",
                new BaseMachineRecipe(
                        new Input(
                                new IC2InputItemStack(container)
                        ),
                        new RecipeOutput(nbt, CraftTweakerMC.getItemStacks(output))
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
            super("furnace");
            this.output = output;
        }

        public void apply() {
            IUCore.removing_list.add(Arrays.asList(CraftTweakerMC.getItemStacks(output)));
        }


    }

}
