package com.denfop.tiles.mechanism.blastfurnace.api;

import com.denfop.IUItem;
import com.denfop.invslot.InvSlot;
import com.denfop.items.resource.ItemIngots;
import com.denfop.tiles.mechanism.blastfurnace.block.TileBlastFurnaceMain;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


public class InvSlotBlastFurnace extends InvSlot {


    private int stackSizeLimit;

    public InvSlotBlastFurnace(TileBlastFurnaceMain base1) {
        super(base1, TypeItemSlot.INPUT, 1);
        this.setStackSizeLimit(64);
    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        if (content.isEmpty()) {
            ((TileBlastFurnaceMain) this.base).outputStack = ItemStack.EMPTY;
        } else {
            if (content.getItem().equals(Items.IRON_INGOT)) {
                ((TileBlastFurnaceMain) this.base).outputStack = IUItem.advIronIngot;
            } else {
                if (content.getItem() instanceof ItemIngots && IUItem.iuingot.getMeta((ItemIngots) content.getItem()) == 3) {
                    ((TileBlastFurnaceMain) this.base).outputStack = new ItemStack(IUItem.crafting_elements.getStack(480));
                } else {
                    ((TileBlastFurnaceMain) this.base).outputStack = new ItemStack(IUItem.crafting_elements.getStack(479));

                }
            }
        }
        return content;
    }

    public boolean accepts(ItemStack itemStack, final int index) {

        return itemStack
                .getItem()
                .equals(Items.IRON_INGOT) || (itemStack.getItem() instanceof ItemIngots && IUItem.iuingot.getMeta((ItemIngots) itemStack.getItem()) == 3) || (itemStack
                .getItem()
                .equals(IUItem.plastic_plate.getItem()));
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }


}
