package com.denfop.invslot;

import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;

public class InvSlotBook extends InvSlot {

    private int stackSizeLimit;

    public InvSlotBook(TileEntityInventory base1) {
        super(base1, "book", InvSlot.Access.I, 3, InvSlot.InvSide.TOP);

        this.stackSizeLimit = 1;
    }

    public boolean accepts(ItemStack itemStack) {
        if (itemStack != null && itemStack.getItem() instanceof ItemEnchantedBook) {
            NBTTagList bookNBT = ((ItemEnchantedBook) itemStack.getItem()).getEnchantments(itemStack);
            if (bookNBT.tagCount() == 1) {
                short id = bookNBT.getCompoundTagAt(0).getShort("id");

                return id == 16 || id == 21 || id == 20 || id == 11;
            }
        }
        return false;
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

}
