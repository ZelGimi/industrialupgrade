package com.denfop.ssp.molecular;

import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import net.minecraft.item.ItemStack;

public interface IMolecularTransformerRecipeManager extends
        IMachineRecipeManager<IMolecularTransformerRecipeManager.Input, ItemStack, ItemStack> {

    IMolecularTransformerRecipeManager RECIPES = new MolecularTransformerRecipeManager();

    boolean addRecipe(IRecipeInput paramIRecipeInput, int paramInt, ItemStack paramItemStack, boolean paramBoolean);

    int getTotalEUNeeded(ItemStack paramItemStack);

    final class Input {

        public final IRecipeInput input;

        public final int totalEU;

        public Input(IRecipeInput input, int totalEU) {
            this.input = input;
            this.totalEU = totalEU;
        }

        public String toString() {
            return "MTInput<" + this.input + ", " + this.totalEU + '>';
        }

    }

}
