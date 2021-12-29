package com.denfop.invslot;


import com.denfop.IUItem;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InvSlotSolidMatter extends InvSlot {

    private int stackSizeLimit;

    public InvSlotSolidMatter(TileEntityInventory base1) {
        super(base1, "input5", InvSlot.Access.IO, 9, InvSlot.InvSide.TOP);

        this.stackSizeLimit = 1;
    }

    public boolean accepts(ItemStack itemStack) {
        return itemStack.getItem().equals(Item.getItemFromBlock(IUItem.solidmatter));
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

    public double getMaxEnergy() {
        double maxEnergy = 0;
        for (int i = 0; i < size(); i++) {
            if (get(i) != null) {
                maxEnergy += 1E5D;
            }

        }
        return maxEnergy;
    }

}
