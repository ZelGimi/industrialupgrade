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
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.MachineRecipe;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@ZenClass("mods.industrialupgrade.compressor")
@ModOnly("industrialupgrade")
@ZenRegister
public class CTCompressor {

    @ZenMethod
    public static void addRecipe(IItemStack output, IIngredient container) {
        Recipes.recipes.addRecipe(
                "compressor",
                new BaseMachineRecipe(
                        new Input(
                                new IC2RecipeInput(container)
                        ),
                        new RecipeOutput(null, CraftTweakerMC.getItemStacks(output))
                )
        );
        ic2.api.recipe.Recipes.compressor.addRecipe(new IC2RecipeInput(container), null, false,
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
            super("compressor");
            this.output = output;
        }

        public void apply() {
            Recipes.recipes.removeRecipe("compressor", new RecipeOutput(null, CraftTweakerMC.getItemStacks(output)));
            final Iterable<? extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> recipe =
                    ic2.api.recipe.Recipes.compressor.getRecipes();
            final ItemStack[] output1 = CraftTweakerMC.getItemStacks(output);
            final Iterator<? extends MachineRecipe<IRecipeInput, Collection<ItemStack>>> iter = recipe.iterator();
            while (iter.hasNext()) {
                MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe1 = iter.next();
                List<ItemStack> list = (List<ItemStack>) recipe1.getOutput();
                if (list.get(0).isItemEqual(output1[0])) {
                    iter.remove();
                    break;
                }
            }
        }

        protected String getRecipeInfo() {
            return LogHelper.getStackDescription(this.output);
        }

    }

}
