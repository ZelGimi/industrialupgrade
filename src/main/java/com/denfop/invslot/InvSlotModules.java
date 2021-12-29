package com.denfop.invslot;


import com.denfop.items.modules.ItemEntityModule;
import com.denfop.utils.CapturedMob;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

public class InvSlotModules extends InvSlot {

    private int stackSizeLimit;

    public InvSlotModules(TileEntityInventory base1) {
        super(base1, "modules", InvSlot.Access.I, 20, InvSlot.InvSide.TOP);

        this.stackSizeLimit = 1;
    }

    public boolean accepts(ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemEntityModule)) {
            return false;
        }
        if (itemStack.getItemDamage() == 0) {
            return false;
        }


        return CapturedMob.containsSoul(itemStack);
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

}
