package com.denfop.integration.crafttweaker;

import com.blamejared.ModTweaker;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import com.denfop.api.Recipes;
import com.denfop.api.recipe.BaseMachineRecipe;
import com.denfop.api.recipe.Input;
import com.denfop.api.recipe.RecipeOutput;
import com.denfop.utils.ModUtils;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Collection;
import java.util.Iterator;

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
        ModTweaker.LATE_REMOVALS.add(new Remove(output));
    }


    private static class Remove extends BaseAction {

        private final IItemStack input;

        public Remove(IItemStack input) {
            super("centrifuge");
            this.input = input;
        }

        public void apply() {
            Recipes.recipes.removeRecipe("centrifuge", CraftTweakerMC.getItemStacks(input)[0]);
            final Iterable<? extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> recipe =
                    ic2.api.recipe.Recipes.centrifuge.getRecipes();
            final ItemStack[] input1 = CraftTweakerMC.getItemStacks(input);
            final Iterator<? extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> iter = recipe.iterator();
            while (iter.hasNext()) {
                MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe1 = iter.next();

                if (recipe1.getInput().matches(input1[0])) {
                    iter.remove();
                    break;
                }
            }
        }

        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(this.input);
        }

    }

}
