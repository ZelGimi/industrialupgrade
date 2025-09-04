package com.denfop.blockentity.mechanism.blastfurnace.api;

import com.denfop.IUItem;
import com.denfop.blockentity.mechanism.blastfurnace.block.BlockEntityBlastFurnaceMain;
import com.denfop.inventory.Inventory;
import com.denfop.items.resource.ItemIngots;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


public class InventoryBlastFurnace extends Inventory {


    private int stackSizeLimit;

    public InventoryBlastFurnace(BlockEntityBlastFurnaceMain base1) {
        super(base1, TypeItemSlot.INPUT, 1);
        this.setStackSizeLimit(64);
    }

    @Override
    public ItemStack set(final int index, final ItemStack content) {
        super.set(index, content);
        if (content.isEmpty()) {
            ((BlockEntityBlastFurnaceMain) this.base).outputStack = ItemStack.EMPTY;
        } else {
            if (content.getItem().equals(Items.IRON_INGOT)) {
                ((BlockEntityBlastFurnaceMain) this.base).outputStack = IUItem.advIronIngot;
            } else {
                if (content.getItem() instanceof ItemIngots && IUItem.iuingot.getMeta((ItemIngots) content.getItem()) == 3) {
                    ((BlockEntityBlastFurnaceMain) this.base).outputStack = new ItemStack(IUItem.crafting_elements.getStack(480));
                } else {
                    ((BlockEntityBlastFurnaceMain) this.base).outputStack = new ItemStack(IUItem.crafting_elements.getStack(479));

                }
            }
        }
        return content;
    }

    public boolean canPlaceItem(final int index, ItemStack itemStack) {

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
