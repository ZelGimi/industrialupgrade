package com.denfop.tiles.mechanism.blastfurnace.api;

import com.denfop.invslot.InvSlot;
import com.denfop.tiles.mechanism.blastfurnace.block.TileBlastFurnaceMain;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;


public class InvSlotBlastFurnace extends InvSlot {


    private int stackSizeLimit;

    public InvSlotBlastFurnace(TileBlastFurnaceMain base1) {
        super(base1, TypeItemSlot.INPUT, 1);
        this.setStackSizeLimit(64);
    }


    public boolean accepts(ItemStack itemStack, final int index) {

        return itemStack.getItem().equals(Items.IRON_INGOT);
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }


}
