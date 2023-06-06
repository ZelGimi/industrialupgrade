package com.denfop.invslot;

import com.denfop.tiles.base.IInventorySlotHolder;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.MachineRecipeResult;
import ic2.core.item.upgrade.ItemUpgradeModule;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;

public abstract class InvSlotProcessableStandard<RI, RO, I> extends InvSlotConsumable {

    protected IMachineRecipeManager<RI, RO, I> recipeManager;

    public InvSlotProcessableStandard(
            IInventorySlotHolder<?> base,
            String name,
            int count,
            IMachineRecipeManager<RI, RO, I> recipeManager
    ) {
        super(base, name, count);
        this.recipeManager = recipeManager;
    }

    public boolean accepts(ItemStack stack, final int index) {
        if (stack.getItem() instanceof ItemUpgradeModule) {
            return false;
        } else {
            ItemStack tmp = StackUtil.copyWithSize(stack, 2147483647);
            return this.getOutputFor(this.getInput(tmp), true) != null;
        }
    }

    public MachineRecipeResult<RI, RO, I> process() {
        ItemStack input = this.get();
        return StackUtil.isEmpty(input) && !this.allowEmptyInput() ? null : this.getOutputFor(this.getInput(input), false);
    }

    public void consume(MachineRecipeResult<RI, RO, I> result) {
        if (result == null) {
            throw new NullPointerException("null result");
        } else {
            ItemStack input = this.get();
            if (StackUtil.isEmpty(input) && !this.allowEmptyInput()) {
                throw new IllegalStateException("consume from empty slot");
            } else {
                this.setInput(result.getAdjustedInput());
            }
        }
    }

    public void setRecipeManager(IMachineRecipeManager<RI, RO, I> recipeManager) {
        this.recipeManager = recipeManager;
    }

    protected boolean allowEmptyInput() {
        return false;
    }

    protected MachineRecipeResult<RI, RO, I> getOutputFor(I input, boolean forAccept) {
        return this.recipeManager.apply(input, forAccept);
    }

    protected abstract I getInput(ItemStack var1);

    protected abstract void setInput(I var1);

}
