package com.denfop.invslot;

import com.denfop.api.inv.IInvSlotProcessable;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.MachineRecipeResult;
import ic2.api.recipe.RecipeOutput;
import ic2.core.block.IInventorySlotHolder;
import ic2.core.block.invslot.InvSlot;
import ic2.core.item.upgrade.ItemUpgradeModule;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;

import java.util.List;

public class InvSlotProcessable extends InvSlot implements IInvSlotProcessable {

    private IMachineRecipeManager recipeManager;

    public InvSlotProcessable(
            final IInventorySlotHolder<?> base,
            final String name,
            final IMachineRecipeManager recipeManager,
            final int count
    ) {
        super(base, name, Access.I, count);
        this.recipeManager = recipeManager;
    }

    @Override
    public ItemStack get1(final int i) {
        return this.get(i);
    }

    public boolean accepts(ItemStack stack) {
        if (stack.getItem() instanceof ItemUpgradeModule) {
            return false;
        } else {
            ItemStack tmp = StackUtil.copyWithSize(stack, 2147483647);
            return this.getOutputFor(tmp, true) != null;
        }
    }


    public RecipeOutput process() {
        ItemStack input = this.get();
        if (input == null) {
            return null;
        } else {
            MachineRecipeResult output = this.getOutputFor(input, false);
            if (output == null) {
                return null;
            } else {
                if (output.getRecipe().getOutput() instanceof List) {
                    List<ItemStack> stack = (List<ItemStack>) output.getRecipe().getOutput();
                    return new RecipeOutput(output.getRecipe().getMetaData(), stack);
                } else {
                    return new RecipeOutput(output.getRecipe().getMetaData(), (ItemStack) output.getRecipe().getOutput());

                }
            }
        }
    }

    public void consume() {
        ItemStack input = this.get();
        if (input == null) {
            throw new IllegalStateException("consume from empty slot");
        } else {
            MachineRecipeResult output = this.getOutputFor(input, true);


            if (output == null) {
                throw new IllegalStateException("consume without a processing result");
            } else {
                this.put((ItemStack) output.getAdjustedInput());


            }
        }
    }

    protected MachineRecipeResult getOutputFor(ItemStack input, boolean adjustInput) {
        return this.recipeManager.apply(input, adjustInput);
    }

    public void setRecipeManager(IMachineRecipeManager recipeManager) {
        this.recipeManager = recipeManager;
    }
//


}
