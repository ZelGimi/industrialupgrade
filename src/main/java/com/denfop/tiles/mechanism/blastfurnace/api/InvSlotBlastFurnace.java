package com.denfop.tiles.mechanism.blastfurnace.api;

import com.denfop.tiles.mechanism.blastfurnace.block.TileEntityBlastInputItem;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;


public class InvSlotBlastFurnace extends InvSlot {


    private int stackSizeLimit;

    public InvSlotBlastFurnace(TileEntityBlastInputItem base1) {
        super(base1, "input", InvSlot.Access.I, 1, InvSide.ANY);
        this.setStackSizeLimit(64);
    }


    public boolean accepts(ItemStack itemStack) {

        return itemStack.getItem().equals(Items.IRON_INGOT);
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }


}
