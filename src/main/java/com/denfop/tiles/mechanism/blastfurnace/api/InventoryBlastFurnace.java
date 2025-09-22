package com.denfop.tiles.mechanism.blastfurnace.api;

import com.denfop.IUItem;
import com.denfop.invslot.Inventory;
import com.denfop.items.resource.ItemIngots;
import com.denfop.tiles.mechanism.blastfurnace.block.TileBlastFurnaceMain;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;


public class InventoryBlastFurnace extends Inventory {


    private int stackSizeLimit;

    public InventoryBlastFurnace(TileBlastFurnaceMain base1) {
        super(base1, TypeItemSlot.INPUT, 1);
        this.setInventoryStackLimit(64);
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        if (content.isEmpty()) {
            ((TileBlastFurnaceMain) this.base).outputStack = ItemStack.EMPTY;
        } else {
            int meta = content.getItemDamage();
            if (content.getItem().equals(Items.IRON_INGOT)) {
                ((TileBlastFurnaceMain) this.base).outputStack = IUItem.advIronIngot;
            } else {
                if (content.getItem() instanceof ItemIngots && meta == 3) {
                    ((TileBlastFurnaceMain) this.base).outputStack = new ItemStack(IUItem.crafting_elements, 1, 480);
                } else {
                    ((TileBlastFurnaceMain) this.base).outputStack = new ItemStack(IUItem.crafting_elements, 1, 479);

                }
            }
        }
    }

    public boolean isItemValidForSlot(final int index, ItemStack itemStack) {
        int meta = itemStack.getItemDamage();
        return itemStack
                .getItem()
                .equals(Items.IRON_INGOT) || (itemStack.getItem() instanceof ItemIngots && meta == 3) || (itemStack
                .getItem()
                .equals(IUItem.plastic_plate));
    }

    public int getInventoryStackLimit() {
        return this.stackSizeLimit;
    }

    public void setInventoryStackLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }


}
