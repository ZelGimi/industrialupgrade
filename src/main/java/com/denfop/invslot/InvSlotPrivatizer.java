package com.denfop.invslot;


import com.denfop.items.modules.ItemAdditionModule;
import com.denfop.items.modules.ItemEntityModule;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

public class InvSlotPrivatizer extends InvSlot {

    private final int type;
    private int stackSizeLimit;

    public InvSlotPrivatizer(TileEntityInventory base1, String name, int type, int count) {
        super(base1, name, InvSlot.Access.IO, count, InvSlot.InvSide.TOP);
        this.stackSizeLimit = 1;
        this.type = type;
    }

    public boolean accepts(ItemStack itemStack) {
        if (type == 0) {
            return itemStack.getItem() instanceof ItemEntityModule && itemStack.getItemDamage() == 0;
        } else {
            return itemStack.getItem() instanceof ItemAdditionModule && itemStack.getItemDamage() == 0;
        }
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

}
